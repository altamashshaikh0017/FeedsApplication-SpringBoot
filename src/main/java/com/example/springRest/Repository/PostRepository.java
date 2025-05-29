package com.example.springRest.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springRest.Model.Admin;
import com.example.springRest.Model.Post;
@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

	List<Post> findByAuthor(Admin author);
	List<Post> findByAuthorOrderByCreatedDateDesc(Admin author);
	List<Post> findAllByOrderByCreatedDateDesc();

}
