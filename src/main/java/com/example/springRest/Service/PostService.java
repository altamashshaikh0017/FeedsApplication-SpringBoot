package com.example.springRest.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springRest.Model.Admin;
import com.example.springRest.Model.Post;
import com.example.springRest.Repository.AdminRepository;
import com.example.springRest.Repository.PostRepository;
import com.example.springRest.Response.PostReq;

@Service
public class PostService {

	@Autowired
	AdminRepository adminRepository;

	@Autowired
	PostRepository postRepository;

	public String addPost(PostReq postReq, String userName) {
		if(postReq.getTitle() ==  null || postReq.getTitle().isEmpty() ||
				postReq.getContent() == null || postReq.getContent().isEmpty()) {
			return "All required fields must be filled.";
		}
		try {
			Admin author = adminRepository.findByUserName(userName).orElse(null);
			Post post = new Post();
			post.setTitle(postReq.getTitle());
			post.setContent(postReq.getContent());
			post.setCreatedDate(new Date());
			post.setAuthor(author);
			post.setApproved(false);

			postRepository.save(post);
			return "SUCCESS";
		}catch (Exception e) {
			e.printStackTrace();
			return "Error while creating post.";
		}
	}
	
	public List<Post> getPostsByUser(String userName){
		Admin author = adminRepository.findByUserName(userName).orElse(null);
		if (author == null) {
	        return Collections.emptyList();
	    }
		List<Post> postsList = postRepository.findByAuthor(author);
		return postsList;
	}

	public Post getPostById(Long postId) {
		
		return postRepository.findById(postId).orElse(null);
	}

	public void deletePost(Long postId) {
		postRepository.deleteById(postId);
	}

	public List<Post> getAllPosts() {
		return postRepository.findAllByOrderByCreatedDateDesc();
	}

	public List<Post> getPostsByAuthor(Admin currentUser) {
		return postRepository.findByAuthorOrderByCreatedDateDesc(currentUser);
	}
}
