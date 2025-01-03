package com.kosa.tm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosa.tm.domain.auction.Auction;
import com.kosa.tm.domain.auction.AuctionDTO;
import com.kosa.tm.domain.auction.AuctionService;
import com.kosa.tm.domain.auction.websocket.AuctionWebSocketHandler;
import com.kosa.tm.domain.member.MemberRepository;
import com.kosa.tm.domain.product.ProductService;
import com.kosa.tm.domain.want.Want;
import com.kosa.tm.domain.want.WantRepository;
import com.kosa.tm.domain.want.WantService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuctionController {

    @Autowired
    AuctionService auctionService;

    @Autowired
    ProductService productService;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    AuctionWebSocketHandler auctionWebSocketHandler;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WantRepository wantRepository;
    @Autowired
    private WantService wantService;

    // 경매장에 참여하면 경매 현황을 가져온다. (혹은, 웹소켓 사용시, 새로운 addAuction이 불러지면 재로딩용으로 사용)
    @GetMapping("/api/{productId}")
    public List<AuctionDTO> getAllAuctionsByProductId(HttpServletRequest request, @PathVariable Long productId) {
        String csrfToken = request.getHeader("X-CSRF-TOKEN");
        System.out.println("CSRF Token: " + csrfToken);
        List<Auction> auctionList = auctionService.getAuctionsByProductId(productId).reversed();
        List<AuctionDTO> auctionDTOList = new ArrayList<>();
        for (Auction auction : auctionList) {
            AuctionDTO auctionDTO = new AuctionDTO();
            auctionDTO.setProduct_id(productId);
            auctionDTO.setUserEmail(auction.getMember().getEmail());
            auctionDTO.setJoin_time(auction.getJoinTime());
            auctionDTO.setPrice(auction.getCurrentPrice());
            auctionDTOList.add(auctionDTO);
        }

        // 가격(currentPrice) 기준으로 내림차순 정렬
        Collections.sort(auctionDTOList, new Comparator<AuctionDTO>() {
            @Override
            public int compare(AuctionDTO o1, AuctionDTO o2) {
                // 내림차순 정렬
                return Integer.compare((int) o2.getPrice(), (int)o1.getPrice());
            }
        });

        // 정렬된 리스트 반환
        return auctionDTOList;

    }

    @PostMapping("/api/{productId}")
    public AuctionDTO addAuction(@PathVariable Long productId, @RequestBody AuctionDTO auctionDTO) throws JsonProcessingException {

        String decodedEmail = URLDecoder.decode(auctionDTO.getUserEmail(), StandardCharsets.UTF_8);
        System.out.println("accepted member email: " + decodedEmail);
        System.out.println(auctionDTO.getPrice());

        // 경매 데이터를 데이터베이스에 저장
        Auction auction = auctionService.join(decodedEmail, productId, auctionDTO.getPrice());

        // 응답으로 저장된 경매 데이터를 반환
        AuctionDTO responseDTO = new AuctionDTO();
        responseDTO.setProduct_id(productId);
        responseDTO.setUserEmail(auction.getMember().getEmail());
        responseDTO.setJoin_time(auction.getJoinTime());
        responseDTO.setPrice(auction.getCurrentPrice());

        // 웹소켓을 통해 다른 클라이언트에게 메시지 전송
        String message = objectMapper.writeValueAsString(responseDTO);
        auctionWebSocketHandler.broadcast(message);
        return responseDTO;
    }



    @GetMapping("/api/{productId}/winner")
    public ResponseEntity<String> getWinnerByProductId(@PathVariable Long productId) {
        Auction auctionResult = auctionService.findBuyerInfoByAuctionProduct(productId);
        Want want = new Want();
        want.setProduct(auctionResult.getProduct());
        want.setMember(auctionResult.getMember());
        Want wantResult = wantService.createWant(want);
        return ResponseEntity.ok(wantResult.getMember().getName() + "님, 축하합니다. 낙찰 되셨습니다!");
    }

}
