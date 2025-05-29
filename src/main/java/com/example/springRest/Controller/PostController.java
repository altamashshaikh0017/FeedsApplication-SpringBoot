package com.example.springRest.Controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springRest.Entity.User;
import com.example.springRest.Model.Admin;
import com.example.springRest.Model.Post;
import com.example.springRest.Repository.PostRepository;
import com.example.springRest.Response.PostReq;
import com.example.springRest.Service.AdminService;
import com.example.springRest.Service.PostService;

@Controller
public class PostController {

	private static final Logger logger = LogManager.getLogger(PostController.class);

	@Autowired
	PostService postService;

	@Autowired
	AdminService adminService;

	@Autowired
	PostRepository postRepository;

	@GetMapping("/createPost")
	public String showCreatePost(Model model) {
		model.addAttribute("post", new PostReq());
		logger.info("Inside Post Controller::showCreatePost()");
		return "createPost";
	}

	@PostMapping("/createPost")
	public String submitPost(@ModelAttribute PostReq postReq, Model model, Principal principal) {
		String userName = principal.getName();
		String result = postService.addPost(postReq, userName);

		if (!"SUCCESS".equals(result)) {
			model.addAttribute("error", result);
			model.addAttribute("post", postReq);
			return "createPost";
		}

		return "redirect:/viewPosts"; // or redirect as needed
	}


	@GetMapping("/viewPosts")
	public String viewPosts(Model model, Principal principal) {
		logger.info("Inside Post Controller::viewPosts()");
		Admin currentUser = adminService.findByUsername(principal.getName());
		List<Post> posts;
		if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
	        posts = postService.getAllPosts(); // Admin sees everything
	    } else {
	        posts = postService.getPostsByAuthor(currentUser); // Users see only their own posts
	    }
		model.addAttribute("posts", posts);
		if (principal != null) {
			model.addAttribute("loggedInUser", principal.getName()); // set logged-in username
		}
		return "viewPosts";
	}

	@GetMapping("/deletePost/{postId}")
	public String deletePost(@PathVariable Long postId, Principal principal, RedirectAttributes redirectAttributes) {
		logger.info("Inside Post Controller::deletePost()");
		try {
			Post post = postService.getPostById(postId);
			String userName = principal.getName();
			Admin currentUser = adminService.findByUsername(userName);

			if (post != null && (
					post.getAuthor().getUserName().equals(principal.getName()) ||
					currentUser.getRole().equalsIgnoreCase("ADMIN")
					)) {
				postService.deletePost(postId);
				redirectAttributes.addFlashAttribute("message", "Post deleted successfully.");
			} else {
				redirectAttributes.addFlashAttribute("error", "You are not authorized to delete this post.");
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error deleting post.");
		}
		return "redirect:/viewPosts";
	}

	@GetMapping("/editPost/{postId}")
	public String showEditPostForm(@PathVariable Long postId, Model model, Principal principal) {
		Post post = postService.getPostById(postId);
		Admin currentUser = adminService.findByUsername(principal.getName());

		if (post == null || !post.getAuthor().getUserName().equals(currentUser.getUserName())) {
			return "redirect:/viewPosts";  // Restrict editing to author only
		}

		PostReq postReq = new PostReq();
		postReq.setPkPostId(post.getPkPostId());
		postReq.setTitle(post.getTitle());
		postReq.setContent(post.getContent());
		postReq.setAuthor(post.getAuthor());
		postReq.setCreatedDate(post.getCreatedDate());
		postReq.setApproved(post.isApproved());

		model.addAttribute("postReq", postReq);
		return "editPost";
	}

	@PostMapping("/updatePost")
	public String updatePost(@ModelAttribute("postReq") PostReq postReq, Principal principal) {
		Admin currentUser = adminService.findByUsername(principal.getName());
		Post existingPost = postService.getPostById(postReq.getPkPostId());

		if (existingPost == null || !existingPost.getAuthor().getUserName().equals(currentUser.getUserName())) {
			return "redirect:/viewPosts";  // Prevent unauthorized edits
		}

		existingPost.setTitle(postReq.getTitle());
		existingPost.setContent(postReq.getContent());
		existingPost.setCreatedDate(new Date());  // Optional: update modified date
		postRepository.save(existingPost);  // Reuse the save method

		return "redirect:/viewPosts";
	}

}
