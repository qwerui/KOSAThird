package com.kosa.tm.domain.auction;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.member.MemberRepository;
import com.kosa.tm.domain.member.MemberService;
import com.kosa.tm.domain.product.Product;
import com.kosa.tm.domain.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionServiceImpl implements AuctionService{

    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Override
    public List<Auction> getAuctionsByProductId(Long productId) {
        return auctionRepository.findAllByProduct(productService.getProduct(productId).get());
    }

    @Override
    public Auction join(String memberEmail, Long productId, float price){
        System.out.println(memberEmail);
        Auction auction = new Auction();
        Product product = productService.getProduct(productId).get();
        auction.setProduct(product);
        Member member = null;
        if(memberService.findByEmail(memberEmail) != null){
            member = memberService.findByEmail(memberEmail);
        }

        System.out.println(member.getEmail());

        auction.setMember(member);
        auction.setJoinTime(LocalDateTime.now());
        auction.setCurrentPrice(price);

        return auctionRepository.save(auction);
    }

    @Override
    @Transactional
    public Auction findBuyerInfoByAuctionProduct(Long productId){
        Product product = productService.getProduct(productId).get();
        List<Auction> auctions = auctionRepository.findAllByProduct(product);
        float highestPrice = 0;
        Auction highestAuction = null;
        if(auctions.size() > 0){
            for(Auction auction : auctions){
                if (auction.getCurrentPrice() > highestPrice) {
                    highestPrice = auction.getCurrentPrice();
                    highestAuction = auction;
                }
            }
        }

        return highestAuction;
    }

    @Override
    @Transactional
    public AuctionDTO findBuyerInfoDTO(Long productId) {
        AuctionDTO dto = new AuctionDTO();
        var buyer = findBuyerInfoByAuctionProduct(productId);
        dto.setJoin_time(buyer.getJoinTime());
        dto.setPrice(buyer.getCurrentPrice());
        dto.setUserEmail(buyer.getMember().getEmail());
        dto.setProduct_id(buyer.getProduct().getId());
        return dto;
    }

}
