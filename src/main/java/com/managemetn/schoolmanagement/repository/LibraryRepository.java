package com.managemetn.schoolmanagement.repository;

import com.managemetn.schoolmanagement.domain.Library;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Library entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LibraryRepository extends JpaRepository<Library, Long>, JpaSpecificationExecutor<Library> {}
