package com.ms.firstjobapp.review;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies/{companyId}")
public class ReviewController {
    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<Review>> getAllReviews(@PathVariable Long companyId) {
        List<Review> reviews = reviewService.getAllReviews(companyId);
        return   new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping("/reviews")
    public ResponseEntity<String> addReview(@RequestBody Review review, @PathVariable Long companyId) {
        boolean isReviewSaved =reviewService.addReview(companyId,review);
        if(isReviewSaved) {
           return new ResponseEntity<>("Review added successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Company not found", HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long reviewId,@PathVariable Long companyId) {
        Review review = reviewService.getReview(companyId,reviewId);
        return   new ResponseEntity<>(review, HttpStatus.OK);
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<String>  updateReview(@PathVariable Long reviewId,@PathVariable Long companyId,@RequestBody Review review) {
        boolean  isUpdated = reviewService.updateReview(companyId,reviewId,review);
        if(isUpdated)  {
            return new ResponseEntity<>("Review updated sucessfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Company not found ",HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String>  deleteReview(@PathVariable Long reviewId,@PathVariable Long companyId) {
        boolean  isDeleted = reviewService.deleteReview(companyId,reviewId);
        if(isDeleted)  {
            return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Review not deleted ",HttpStatus.NOT_FOUND);
    }


}

/*

GET   /companies/{companyId}/reviews
POST  /companies/{companyId}/reviews
GET  /companies/{companyId}/reviews/{reviewId}
PUT    /companies/{companyId}/reviews/{reviewId}
DELETE /companies/{companyId}/reviews/{reviewI*/