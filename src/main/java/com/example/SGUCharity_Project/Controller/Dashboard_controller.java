package com.example.SGUCharity_Project.Controller;

import com.example.SGUCharity_Project.Model.*;
import com.example.SGUCharity_Project.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class Dashboard_controller {
    @Autowired
    Charitycontent_Repo charitycontentRepo;

    @Autowired
    ArticalDetail_Repo articalDetailRepo;

    @Autowired
    Payment_Repo paymentRepo;

    @Autowired
    CommunityNews_Repo communityNewsRepo;

    @Autowired
    Contact_Repo contactRepo;

    @Autowired
    Note_Repo noteRepo;

    @Autowired
    Statistical_Repo statisticalRepo;

    @Autowired
    ServiceOperations_Repo serviceOperationsRepo;

    // Render ra trang dashboard
    @GetMapping("/manager")
    public String dashboard() {
        return "page_admin/Dashboard";
    }

    @GetMapping("/dashboard_programmanagement")
    public String programmanagement(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "3") int size,
                                    Model model) {
        Pageable pageable = PageRequest.of(page, size); // Xác định số trang và số mục trên mỗi trang
        Page<Artical_model> pageResult = charitycontentRepo.findAll(pageable);

        model.addAttribute("charitycontentModelLists", pageResult.getContent()); // Danh sách các mục của trang hiện tại
        model.addAttribute("currentPage", page); // Trang hiện tại
        model.addAttribute("totalPages", pageResult.getTotalPages()); // Tổng số trang
        model.addAttribute("totalItems", pageResult.getTotalElements()); // Tổng số mục

        return "page_admin/ProgramManagement_admin";
    }

    // Render ra trang quản lý nội dung bài viết
    @GetMapping("/dashboard_articlemanagement")
    public String articlemanagement(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "3") int size,
                                    Model model) {
        Pageable pageable = PageRequest.of(page, size); // Xác định số trang và số mục trên mỗi trang
        Page<Articaldetail_model> pageResult = articalDetailRepo.findAll(pageable);

        model.addAttribute("articaldetailModelLists", pageResult.getContent()); // Danh sách các mục của trang hiện tại
        model.addAttribute("currentPage", page); // Trang hiện tại
        model.addAttribute("totalPages", pageResult.getTotalPages()); // Tổng số trang
        model.addAttribute("totalItems", pageResult.getTotalElements()); // Tổng số mục

        return "page_admin/ArticleManagement_admin";
    }

    @PostMapping("program/delete/{id_delete}")
    public String program_delete(@PathVariable("id_delete") Long id) {
        charitycontentRepo.deleteById(id);
        return "redirect:/dashboard_programmanagement";
    }
    @GetMapping("/insert/program")
    public String insert_program() {
        return "page_admin/CRUD_ProgramManagement/insertProgram";
    }

    // Handle chuc nang insert account
    @PostMapping("/insert/program")
    public String insertProgram(@RequestParam("inputimg") String inputimg, @RequestParam("inputtitle") String inputtitle, @RequestParam("content1") String content1, @RequestParam("content2") String content2,@RequestParam("content3") String content3,  // Nội dung chi tiết 1
                                @RequestParam("imgContent") String imgContent, @RequestParam("imgContent2") String imgContent2  // Ảnh chi tiết

                                ) {  // Nội dung chi tiết 2

        // Tạo một Artical_model mới và lưu nó
        Artical_model artical = new Artical_model();
        artical.setImg(inputimg);
        artical.setTitle(inputtitle);
        artical.setStatus("Đang vận động");
        artical.setDisplaycategory("Mặc định");

        // Lưu Artical_model vào database để có ID
        Artical_model savedArtical = charitycontentRepo.save(artical);

        // Tạo một Articaldetail_model mới và liên kết với Artical_model đã lưu
        Articaldetail_model articalDetail = new Articaldetail_model();
        articalDetail.setContent_1(content1);
        articalDetail.setContent_2(content2);
        articalDetail.setContent_3(content3);
        articalDetail.setImg_content(imgContent);
        articalDetail.setImg_content2(imgContent2);

        // Liên kết với bài viết chính
        articalDetail.setArtical(savedArtical);

        // Lưu Articaldetail_model vào database
        articalDetailRepo.save(articalDetail);

        return "redirect:/dashboard_programmanagement";
    }

    @GetMapping("programmanagement/{id_update}")
    public String programmanagement_update(@PathVariable("id_update") Long id, Model model) {
        Artical_model articalModel = charitycontentRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID" + id));
        Articaldetail_model articaldetailModels = articalDetailRepo.findFirstByArtical_Id(id);
        model.addAttribute("articalModel", articalModel);
        model.addAttribute("articaldetailModels", articaldetailModels);

        return "page_admin/CRUD_ProgramManagement/updateProgram";
    }


    @PostMapping("programmanagement/{id_update}")
    public String handle_programmanagement_update(@RequestParam("inputimg") String inputimg, @RequestParam("inputtitle") String inputtitle, @RequestParam("content1") String content1, @RequestParam("content2") String content2,@RequestParam("content3") String content3,  // Nội dung chi tiết 1
                                @RequestParam("imgContent") String imgContent, @RequestParam("imgContent2") String imgContent2, @PathVariable("id_update") Long id  // Ảnh chi tiết

    ) {  // Nội dung chi tiết 2
        Artical_model artical = charitycontentRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID" + id));
        // Tạo một Artical_model mới và lưu nó
        artical.setImg(inputimg);
        artical.setTitle(inputtitle);
        artical.setStatus("Đang vận động");
        artical.setDisplaycategory("Mặc định");

        // Lưu Artical_model vào database để có ID
        Artical_model savedArtical = charitycontentRepo.save(artical);

        // Tạo một Articaldetail_model mới và liên kết với Artical_model đã lưu
        Articaldetail_model articalDetail = articalDetailRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID" + id));
        articalDetail.setContent_1(content1);
        articalDetail.setContent_2(content2);
        articalDetail.setContent_3(content3);
        articalDetail.setImg_content(imgContent);
        articalDetail.setImg_content2(imgContent2);

        // Liên kết với bài viết chính
        articalDetail.setArtical(savedArtical);

        // Lưu Articaldetail_model vào database
        articalDetailRepo.save(articalDetail);

        return "redirect:/dashboard_programmanagement";
    }


    @PostMapping("/updateStatus")
    public String updateStatus(@RequestParam("id") Long id, @RequestParam("status") String status) {
        // Tìm đối tượng charitycontentModelList bằng id
        Optional<Artical_model> charityContentOptional = charitycontentRepo.findById(id);
        if (charityContentOptional.isPresent()) {
            Artical_model charityContent = charityContentOptional.get();
            charityContent.setStatus(status); // Cập nhật status
            charitycontentRepo.save(charityContent); // Lưu lại thay đổi
        }
        return "redirect:/dashboard_programmanagement"; // Chuyển hướng về trang mà bạn muốn
    }



    @PostMapping("/updateDisplayCategory")
    public String updateDisplayCategory(@RequestParam("id") Long id, @RequestParam("display") String display) {
        Optional<Artical_model> charityContentOptional = charitycontentRepo.findById(id);
        if (charityContentOptional.isPresent()) {
            Artical_model charityContent = charityContentOptional.get();
            charityContent.setDisplaycategory(display);
            charitycontentRepo.save(charityContent);
        }
        return "redirect:/dashboard_programmanagement";
    }

    @GetMapping("dashboard_revenuemanagement")
    public String revenue(Model model) {
        List<Payment_model> paymentModels = paymentRepo.findAll();
        model.addAttribute("paymentModel",paymentModels);
        return "page_admin/RevenueManagement_admin";
    }

    @PostMapping("/displayrevenue")
    public String displayrevenue(@RequestParam("id") Long id, @RequestParam("display") int display, Model model) {
        // Tìm đối tượng Payment_model theo ID
        Optional<Payment_model> paymentModelOp = paymentRepo.findById(id);

        if (paymentModelOp.isPresent()) {
            Payment_model paymentModel = paymentModelOp.get();
            paymentModel.setDisplay(display);
            paymentRepo.save(paymentModel);
        }
        return "redirect:/dashboard_revenuemanagement";
    }

    // Handle chuc nang delete revenue
    @PostMapping("revenue/delete/{id_delete}")
    public String revenue_delete(@PathVariable("id_delete") Long id) {
        paymentRepo.deleteById(id);
        return "redirect:/dashboard_revenuemanagement";
    }

    @GetMapping("/dashboard_newsmanagement")
    public String newsmanagement(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "3") int size,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size); // Xác định số trang và số mục trên mỗi trang
        Page<Communitynews_model> pageResult = communityNewsRepo.findAll(pageable);

        model.addAttribute("communityNewsModels", pageResult.getContent()); // Danh sách các mục của trang hiện tại
        model.addAttribute("currentPage", page); // Trang hiện tại
        model.addAttribute("totalPages", pageResult.getTotalPages()); // Tổng số trang
        model.addAttribute("totalItems", pageResult.getTotalElements()); // Tổng số mục

        return "page_admin/NewsManagement_admin";
    }


    @GetMapping("newsmanagement/{id_update}")
    public String newsmanagement_update(@PathVariable("id_update") Long id, Model model) {
        Communitynews_model communitynewsModel = communityNewsRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID" + id));
        model.addAttribute("communitynewsModel", communitynewsModel);
        return "page_admin/CRUD_NewsManagement/updateNews";
    }

    @PostMapping("newsmanagement/{id_update}")
    public String handle_newsmanagement_update(@RequestParam("inputtitlenews") String inputtitlenews, @RequestParam("inputimgnews") String inputimgnews, @RequestParam("inputsubtitlenews") String inputsubtitlenews,
                                 @RequestParam("inputurlartical") String inputurlartical, @PathVariable("id_update") Long id) {
        Communitynews_model communitynewsModel = communityNewsRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID" + id));
        communitynewsModel.setTitle_news(inputtitlenews);
        communitynewsModel.setImg_news(inputimgnews);
        communitynewsModel.setSub_titlenews(inputsubtitlenews);
        communitynewsModel.setDate_update(LocalDate.now());
        communitynewsModel.setUrl_artical(inputurlartical);

        communityNewsRepo.save(communitynewsModel);

        return "redirect:/dashboard_newsmanagement";
    }

    @PostMapping("newsmanagement/delete/{id_delete}")
    public String newsmanagement_delete(@PathVariable("id_delete") Long id) {
        communityNewsRepo.deleteById(id);
        return "redirect:/dashboard_newsmanagement";
    }

    @GetMapping("/insert/news")
    public String insertnews() {
        return "page_admin/CRUD_NewsManagement/insertNews";
    }

    @PostMapping("/insert/news")
    public String insertnews(@RequestParam("inputtitlenews") String inputtitlenews, @RequestParam("inputimgnews") String inputimgnews, @RequestParam("inputsubtitlenews") String inputsubtitlenews,
                                @RequestParam("inputurlartical") String inputurlartical) {

        Communitynews_model communitynewsModel = new Communitynews_model();
        communitynewsModel.setTitle_news(inputtitlenews);
        communitynewsModel.setImg_news(inputimgnews);
        communitynewsModel.setSub_titlenews(inputsubtitlenews);
        communitynewsModel.setDate_update(LocalDate.now());
        communitynewsModel.setUrl_artical(inputurlartical);

        communityNewsRepo.save(communitynewsModel);

        return "redirect:/dashboard_newsmanagement";
    }


    @GetMapping("dashboard_servicemanagement")
    public String servicemanagement(Model model) {
        List<Service_model> serviceModels = serviceOperationsRepo.findAll();
        model.addAttribute("serviceModels", serviceModels);
        return "page_admin/ServiceManagement_admin";
    }

    @GetMapping("/insert/service")
    public String insertservice() {
        return "page_admin/CRUD_ServiceManagement/insertService";
    }

    @PostMapping("/insert/service")
    public String insertservice(@RequestParam("inputtitleservice") String inputtitleservice, @RequestParam("inputimgservice") String inputimgservice, @RequestParam("inputsubtitleservice") String inputsubtitleservice,
                             @RequestParam("inputurlarticalservice") String inputurlarticalservice) {

        Service_model serviceModel = new Service_model();
        serviceModel.setTitle_service(inputtitleservice);
        serviceModel.setImg_thumbnail(inputimgservice);
        serviceModel.setSubtitle_service(inputsubtitleservice);
        serviceModel.setUrlartical_service(inputurlarticalservice);

        serviceOperationsRepo.save(serviceModel);

        return "redirect:/dashboard_servicemanagement";
    }

    @GetMapping("/servicemanagement/{id_update}")
    public String servicemanagement_update(@PathVariable("id_update") Long id, Model model) {
        Service_model serviceModel = serviceOperationsRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid service ID: " + id));
        model.addAttribute("serviceModels", serviceModel);
        return "page_admin/CRUD_ServiceManagement/updateService";
    }

    @PostMapping("/servicemanagement/{id_update}")
    public String handle_servicemanagement_update(@RequestParam("inputtitleservice") String inputtitleservice, @RequestParam("inputimgservice") String inputimgservice, @RequestParam("inputsubtitleservice") String inputsubtitleservice,
                                                  @RequestParam("inputurlarticalservice") String inputurlarticalservice, @PathVariable("id_update") Long id) {
        Service_model serviceModel = serviceOperationsRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID" + id));
        serviceModel.setTitle_service(inputtitleservice);
        serviceModel.setImg_thumbnail(inputimgservice);
        serviceModel.setSubtitle_service(inputsubtitleservice);
        serviceModel.setUrlartical_service(inputurlarticalservice);

        serviceOperationsRepo.save(serviceModel);

        return "redirect:/dashboard_servicemanagement";
    }

    @PostMapping("servicemanagement/delete/{id_delete}")
    public String servicemanagement_delete(@PathVariable("id_delete") Long id) {
        serviceOperationsRepo.deleteById(id);
        return "redirect:/dashboard_servicemanagement";
    }

    @GetMapping("dashboard_contact")
    public String contact(Model model) {
        List<Contact_model> contactModels = contactRepo.findAll();
        model.addAttribute("contactModels",contactModels);
        return "page_admin/ContactManagement_admin";
    }

    @PostMapping("contact/delete/{id_delete}")
    public String contact_delete(@PathVariable("id_delete") Long id) {
        contactRepo.deleteById(id);
        return "redirect:/dashboard_contact";
    }

    @GetMapping("dashboard_note")
    public String note(Model model) {
        List<Note_model> notes = noteRepo.findAll();
        model.addAttribute("notes", notes);
        return "page_admin/NoteManagement_admin";
    }

    @PostMapping("/addNote")
    public String addNote(@RequestParam String date, @RequestParam String content) {
        Note_model note = new Note_model();
        note.setDate(date);
        note.setContent(content);
        noteRepo.save(note);
        return "redirect:/dashboard_note";
    }

    @GetMapping("/note")
    public ResponseEntity<List<Note_model>> getNotesByDate(@RequestParam String date) {
        List<Note_model> notes = noteRepo.findByDate(date);
        return ResponseEntity.ok(notes);
    }

    @DeleteMapping("/deleteNote/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteRepo.deleteById(id);
        return ResponseEntity.noContent().build(); // Trả về mã 204 No Content
    }

    @GetMapping("dashboard_statistical")
    public String statistical() {
        return "page_admin/StatisticalManagement_admin";
    }

    @PostMapping("/insert/statistical")
    public String insertstatistical(@RequestParam("successfulproject") String successfulproject, @RequestParam("participants") String participants,
                                    @RequestParam("donationamount") String donationamount,
                                    @RequestParam("eventsorganized") String eventsorganized) {

        Statistical_model statisticalModel = new Statistical_model();
        statisticalModel.setSuccessfulproject(successfulproject);
        statisticalModel.setParticipants(participants);
        statisticalModel.setDonationamount(donationamount);
        statisticalModel.setEventsorganized(eventsorganized);
        statisticalModel.setDateupdated(LocalDate.now());

        statisticalRepo.save(statisticalModel);

        return "redirect:/dashboard_statistical";
    }
}
