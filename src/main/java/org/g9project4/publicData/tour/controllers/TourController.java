package org.g9project4.publicData.tour.controllers;

import lombok.RequiredArgsConstructor;
import org.g9project4.config.controllers.ApiConfig;
import org.g9project4.config.service.ConfigInfoService;
import org.g9project4.global.ListData;
import org.g9project4.global.Pagination;
import org.g9project4.global.Utils;
import org.g9project4.global.exceptions.BadRequestException;
import org.g9project4.global.exceptions.ExceptionProcessor;
import org.g9project4.global.exceptions.TourPlaceNotFoundException;
import org.g9project4.global.rests.gov.detailapi.DetailItem;
import org.g9project4.global.rests.gov.detailpetapi.DetailPetItem;
import org.g9project4.publicData.greentour.entities.GreenPlace;
import org.g9project4.publicData.tour.constants.ContentType;
import org.g9project4.publicData.tour.entities.PlaceDetail;
import org.g9project4.publicData.tour.entities.TourPlace;
import org.g9project4.publicData.tour.repositories.TourPlaceRepository;
import org.g9project4.publicData.tour.services.TourDetailInfoService;
import org.g9project4.publicData.tour.services.TourPlaceInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tour")
@RequiredArgsConstructor
public class TourController implements ExceptionProcessor {
    private final TourPlaceRepository tourPlaceRepository;
    private final TourPlaceInfoService placeInfoService;
    private final TourDetailInfoService detailInfoService;
    private final ConfigInfoService configInfoService;

    private final Utils utils;

    @ModelAttribute("apiKeys")
    public ApiConfig getApiKeys() {
        return configInfoService.get("apiConfig", ApiConfig.class).orElseGet(ApiConfig::new);
    }

    private void addListProcess(Model model, ListData<TourPlace> data) {
        Pagination pagination = data.getPagination();
        //pagination.setBaseURL();
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", pagination);
    }

    private void commonProcess(String mode, Model model) {
        List<String> addCss = new ArrayList<>();
        List<String> addCommonCss = new ArrayList<>();
        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();
        if (mode.equals("list")) {
            addCss.addAll(List.of("tour/list", "tour/_typelist"));
            addScript.add("tour/locBased");
        } else if (mode.equals("geolocation")) {
            addCss.addAll(List.of("tour/list", "tour/_typelist"));
            addScript.add("tour/locBased");
        } else if (mode.equals("detail")) {
            addCss.add("tour/map");
            addScript.add("tour/detailMap");
            addCommonScript.add("map");
        } else if (mode.equals("view")) {
            addCss.addAll(List.of("tour/map", "tour/sidebar"));
            addScript.addAll(List.of("tour/map", "tour/sidebar"));
        }
        model.addAttribute("addCss", addCss);
        model.addAttribute("addCommonCss", addCommonCss);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
    }


    private String greenList(TourPlaceSearch search, Model model) {
        ListData<GreenPlace> items = null;
        try {
            items = placeInfoService.getGreenList(search);
        } catch (Exception e) {
            throw new TourPlaceNotFoundException();
        }
        commonProcess("list", model);
        model.addAttribute("items", items.getItems());
        model.addAttribute("pagination", items.getPagination());

        return utils.tpl("tour/list");
    }

    @GetMapping("/popup")
    public String popup(Model model) {
        return utils.tpl("tour/popup");
    }

    @GetMapping("/view")
    public String view(Model model, @ModelAttribute TourPlaceSearch search) {
        search.setContentType(null);
        ListData<TourPlace> data = placeInfoService.getSearchedList(search);
        commonProcess("view", model);
        addListProcess(model, data);
        return utils.tpl("tour/map");
    }


    @GetMapping("/list")
    public String list(@ModelAttribute TourPlaceSearch search, Model model) {
        // 기본 정렬 기준과 방향 설정
        String sortField = search.getSort() != null ? search.getSort() : "contentId";
        String sortDirection = search.getSortDirection() != null ? search.getSortDirection() : "asc";

        // 특정 필드에 대해 정렬 방향을 설정
        if ("contentId".equals(sortField)) {
            sortDirection = "asc"; // contentId는 오름차순
        } else if ("placePointValue".equals(sortField)) {
            sortDirection = "desc"; // placePointValue는 내림차순
        }

        search.setSort(sortField);
        search.setSortDirection(sortDirection);

        // 데이터 가져오기
        ListData<TourPlace> data = placeInfoService.getSearchedList(search);

        // 공통 처리 및 데이터 추가
        commonProcess("list", model);
        addListProcess(model, data);
        // 템플릿 반환
        return utils.tpl("tour/list"); // 템플릿 경로 수정 필요시 조정
    }


    @GetMapping("/list/{type}")
    public String list(@PathVariable("type") String type, @ModelAttribute TourPlaceSearch search, Model model) {
        try {
            if (StringUtils.hasText(type)) search.setContentType(utils.typeCode(type));
            // ContentType을 설정
            search.setContentType(utils.typeCode(type));

            // 정렬 기준 및 방향 처리
            String sortField = (search.getSort() != null && !search.getSort().isEmpty()) ? search.getSort() : "contentId";
            String sortDirection = (search.getSortDirection() != null && !search.getSortDirection().isEmpty()) ? search.getSortDirection() : "asc";

            // 설정된 정렬 기준과 방향을 search 객체에 설정
            search.setSort(sortField);
            search.setSortDirection(sortDirection);

            // GreenTour 타입에 대한 별도 처리
            if (search.getContentType() == ContentType.GreenTour) {
                return greenList(search, model);
            }
            ListData<TourPlace> data = placeInfoService.getSearchedList(search);
            commonProcess("list", model);
            addListProcess(model, data);
            return utils.tpl("tour/list");
        } catch (BadRequestException e) {
            e.printStackTrace();
            return "redirect:" + utils.redirectUrl("/tour/list");
        }
    }

    @GetMapping("/distance/list")
    public String distanceList(/*@PathVariable(name = "type", required = false) String type,*/ @RequestParam("latitude") Double latitude,
                                                                                               @RequestParam("longitude") Double longitude,
                                                                                               @RequestParam(name = "radius", required = false) Integer radius, @ModelAttribute TourPlaceSearch search, Model model) {
        try {
            commonProcess("geolocation", model);
            search.setLatitude(latitude);
            search.setLongitude(longitude);
            search.setRadius(radius);
            ListData<TourPlace> data = placeInfoService.getLocBasedList(search);
            addListProcess(model, data);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TourPlaceNotFoundException();
        }
        return utils.tpl("tour/list");
    }

    @GetMapping("/detail/{contentId}")
    public String detail(@PathVariable("contentId") Long contentId, Model model) {
        PlaceDetail<DetailItem, DetailPetItem> item = detailInfoService.getDetail(contentId);
        commonProcess("detail", model);
        model.addAttribute("items", item);
        return utils.tpl("tour/detail");
    }

    @GetMapping("/list/loc/{type}")
    public String distanceList(@PathVariable("type") String type, @ModelAttribute TourPlaceSearch search, Model model) {
        try{
            commonProcess("getLocation",model);
            search.setLatitude(37.566826);
            search.setLongitude(126.9786567);
            search.setRadius(1000);
           // ListData<TourPlace> data = placeInfoService.getLocBasedList(search);
          //  addListProcess(model,data);
        }catch(Exception e){
            e.printStackTrace();
            throw new TourPlaceNotFoundException();
        }
        return utils.tpl("tour/list");
    }

}