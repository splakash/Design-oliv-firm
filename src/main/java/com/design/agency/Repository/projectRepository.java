package com.design.agency.Repository;
import com.design.agency.WorkEntity.Projects;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface projectRepository extends JpaRepository<Projects, Long> {

    @Query(value = "SELECT p.cover_image, p.title, p.folder_id, p.id from Project p", nativeQuery = true)
    List<List<String>>findImagesLink();


    @Query(value = "SELECT a.folder_id, a.title from Project a", nativeQuery = true)
    List<List<String>>fieldIdandTitle();
}

