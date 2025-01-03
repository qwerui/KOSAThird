package com.kosa.tm.controller;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.member.MemberRepository;
import com.kosa.tm.domain.product.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class HomeController {
    @Autowired
    ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MemberRepository memberRepository;
    private final CsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();

    @GetMapping("/api/test")
    public String testConnection() {
        return "Backend is working!";
    }

    @GetMapping("/api/ongoing-all")
    public List<ProductBuffer> ongoingAll() {
        List<Product> products = productService.getAllOngoingDeals();
        List<ProductBuffer> buffer = new ArrayList<>();

        for(var p : products){
            var buf = new ProductBuffer();
            buf.setProduct(p);
            buffer.add(buf);
        }

        return buffer;

    }

    @GetMapping("/api/upcoming-all")
    public List<ProductBuffer> upcomingAll() {
        List<Product> products = productService.getAllUpcomingDeals();
        List<ProductBuffer> buffer = new ArrayList<>();

        for(var p : products){
            var buf = new ProductBuffer();
            buf.setProduct(p);
            buffer.add(buf);
        }

        return buffer;
    }


    @GetMapping("/api/lastdeals-all")
    public List<ProductBuffer> lastdealsAll() {
        List<Product> products = productService.getAllLastDeals();
        List<ProductBuffer> buffer = new ArrayList<>();

        for(var p : products){
            var buf = new ProductBuffer();
            buf.setProduct(p);
            buffer.add(buf);
        }

        return buffer;
    }

    @GetMapping("/api/searchItems")
    public List<ProductSearchBuffer> loadSearchData(){
        List<Product> products = productService.getAllOngoingDeals();
        List<ProductSearchBuffer> data = new ArrayList<>();
        for (Product product : products) {
            ProductSearchBuffer item = new ProductSearchBuffer();
            item.setTitle(product.getTitle());
            item.setPrice(product.getPrice());
            item.setCloseTime(product.getCloseTime());
            data.add(item);
        }
        return data;
    }

    @GetMapping("/api/csrf-token")
    public Map<String, String> getCsrfToken(HttpServletRequest request, HttpServletResponse response) {
        CsrfToken csrfToken = csrfTokenRepository.loadToken(request);
        if (csrfToken == null) {
            csrfToken = csrfTokenRepository.generateToken(request);
            csrfTokenRepository.saveToken(csrfToken, request, response);
        }
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("csrfToken", csrfToken.getToken());
        return responseBody;
    }

}