package com.ms.reviewms.review.impl;


import com.ms.reviewms.review.Review;
import com.ms.reviewms.review.ReviewRepository;
import com.ms.reviewms.review.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl  implements ReviewService {
    private ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }


    @Override
    public List<Review> getAllReviews(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public boolean addReview(Long companyId, Review review) {
        if (companyId != null && review != null) {
            review.setCompanyId(companyId);
            reviewRepository.save(review);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    @Override
    public boolean updateReview(Long reviewId, Review updateReview) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review != null && updateReview != null)
    {
        review.setTitle(updateReview.getTitle());
        review.setDescription(updateReview.getDescription());
        review.setRating(updateReview.getRating());
        review.setCompanyId(updateReview.getCompanyId());
        review.setTitle(updateReview.getTitle());

        reviewRepository.save(review);
        return true;
    }
    return false;
}

    @Override
    public boolean deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review != null) {
            review.setCompanyId(null);
            reviewRepository.delete(review);
            return true;
        }
        return false;
    }
}
