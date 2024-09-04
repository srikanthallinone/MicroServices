package com.ms.reviewms.review;

import com.ms.reviewms.review.messaging.ReviewMessageProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private ReviewService reviewService;
    private ReviewMessageProducer reviewMessageProducer;
    public ReviewController(ReviewService reviewService,ReviewMessageProducer reviewMessageProducer) {
        this.reviewService = reviewService;
        this.reviewMessageProducer = reviewMessageProducer;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam Long companyId) {
        List<Review> reviews = reviewService.getAllReviews(companyId);
        return  new ResponseEntity<>(reviews, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<String> addReview(@RequestBody Review review, @RequestParam Long companyId) {
        boolean isReviewSaved =reviewService.addReview(companyId,review);
        if(isReviewSaved) {
            reviewMessageProducer.sendMessage(review);
           return new ResponseEntity<>("Review added successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Company not found", HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long reviewId) {
        Review review = reviewService.getReview(reviewId);
        return   new ResponseEntity<>(review, HttpStatus.OK);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<String>  updateReview(@PathVariable Long reviewId,@RequestBody Review review) {
        boolean  isUpdated = reviewService.updateReview(reviewId,review);
        if(isUpdated)  {
            return new ResponseEntity<>("Review updated sucessfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Company not found ",HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String>  deleteReview(@PathVariable Long reviewId) {
        boolean  isDeleted = reviewService.deleteReview(reviewId);
        if(isDeleted)  {
            return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Review not deleted ",HttpStatus.NOT_FOUND);
    }

    @GetMapping("/averageRating")
    public Double getAverageReview(@RequestParam Long companyId) {
        List<Review> reviewList = reviewService.getAllReviews(companyId);
        return reviewList.stream().mapToDouble(Review:: getRating).average().orElse(0.0);
    }


}

/*

GET   /reviews?companyId={companyId}
POST  /reviews?companyId={companyId}
GET  /reviews/{reviewId}
PUT   /reviews/{reviewId}
DELETE  /reviews/{reviewId}

 */