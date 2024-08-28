package org.g9project4.publicData.tourvisit;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.g9project4.publicData.myvisit.services.TourplaceInterestsPointService;
import org.g9project4.publicData.myvisit.services.TourplacePointMemberService;
import org.g9project4.publicData.tour.controllers.TourPlaceSearch;
import org.g9project4.publicData.tour.entities.TourPlace;
import org.g9project4.publicData.tour.repositories.TourPlaceRepository;
import org.g9project4.publicData.tour.services.ApiUpdateService;
//import org.g9project4.tourvisit.services.TourPlaceRepositoryCustomImpl;
import org.g9project4.publicData.tourvisit.services.*;
//import org.g9project4.tourvisit.services.SigunguTableStatisticService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
//@ActiveProfiles("test")
public class DataTransfer {

    @Autowired
    private TourPlaceRepository tourPlaceRepository;

    @Autowired
    private SidoVisitStatisticService service;

    @Autowired
    private SigunguVisitStatisticService service2;

    @Autowired
    private ApiUpdateService apiUpdateService;

    @Autowired
    private VisitUpdateService visitUpdateService;

    @Autowired
    private SigunguTableStatisticService tableService;


    @Autowired
    private TourplacePointService pointService;


    @PersistenceContext
    private EntityManager em;

    private String skey = "n5fRXDesflWpLyBNdcngUqy1VluCJc1uhJ0dNo4sNZJ3lkkaYkkzSSY9SMoZbZmY7/O8PURKNOFmsHrqUp2glA==";


    @Test
    void test1() {
        service.updateSidoVisit("1D");
        service.updateSidoVisit("1W");
        service.updateSidoVisit("1M");
        service.updateSidoVisit("3M");
        service.updateSidoVisit("6M");
        service.updateSidoVisit("1Y");
    }

    @Test
    void test2() {
        service2.updateVisit("1D");
        service2.updateVisit("1W");
        service2.updateVisit("1M");
        service2.updateVisit("3M");
        service2.updateVisit("6M");
        service2.updateVisit("1Y");
    }

    @Test // tourplace api 다운로드 km 1000개만
    void test3() {
       apiUpdateService.update(skey);
    }

    @Test
    void test4() {
        tableService.update();

    }


    @Test
    void test5() {
        visitUpdateService.update();
    }




    @Test // placepointvalue 계산 4시간
    void test6() {

        pointService.update();

    }






}



