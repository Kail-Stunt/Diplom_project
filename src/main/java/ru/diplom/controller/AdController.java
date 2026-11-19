
package ru.diplom.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.diplom.dto.request.CreateOrUpdateAdRequest;
import ru.diplom.dto.response.AdResponse;
import ru.diplom.dto.response.AdsResponse;
import ru.diplom.dto.response.ExtendedAdResponse;
import ru.diplom.entity.User;
import ru.diplom.service.AdService;
import ru.diplom.service.ImageService;
import ru.diplom.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/ads")
public class AdController {

    private final AdService adService;
    private final UserService userService;
    private final ImageService imageService;

    public AdController(AdService adService, UserService userService, ImageService imageService) {
        this.adService = adService;
        this.userService = userService;
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<AdsResponse> getAllAds() {
        AdsResponse response = adService.getAllAds();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<AdsResponse> getAdsMe() {
        User currentUser = userService.getCurrentUser();
        AdsResponse response = adService.getAdsByUser(currentUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AdResponse> addAd(
            @RequestParam("properties") @Valid CreateOrUpdateAdRequest request,
            @RequestParam("image") MultipartFile image) throws IOException {
        User currentUser = userService.getCurrentUser();
        String imagePath = imageService.storeImage(image);
        AdResponse response = adService.addAd(request, currentUser, imagePath);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAdResponse> getAd(@PathVariable Integer id) {
        ExtendedAdResponse response = adService.getAd(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdResponse> updateAd(
            @PathVariable Integer id,
            @Valid @RequestBody CreateOrUpdateAdRequest request) {
        User currentUser = userService.getCurrentUser();
        AdResponse response = adService.updateAd(id, request, currentUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable Integer id) {
        User currentUser = userService.getCurrentUser();
        adService.removeAd(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/image")
    public ResponseEntity<Void> updateImage(@PathVariable Integer id, @RequestParam("image") MultipartFile image) throws IOException {
        User currentUser = userService.getCurrentUser();
        String imagePath = imageService.storeImage(image);
        adService.updateAdImage(id, imagePath, currentUser);
        return ResponseEntity.ok().build();
    }
}
