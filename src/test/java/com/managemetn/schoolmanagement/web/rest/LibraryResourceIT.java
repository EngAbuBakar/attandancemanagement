package com.managemetn.schoolmanagement.web.rest;

import static com.managemetn.schoolmanagement.domain.LibraryAsserts.*;
import static com.managemetn.schoolmanagement.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.managemetn.schoolmanagement.IntegrationTest;
import com.managemetn.schoolmanagement.domain.Library;
import com.managemetn.schoolmanagement.repository.LibraryRepository;
import com.managemetn.schoolmanagement.service.dto.LibraryDTO;
import com.managemetn.schoolmanagement.service.mapper.LibraryMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LibraryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LibraryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_CODE = 1L;
    private static final Long UPDATED_CODE = 2L;
    private static final Long SMALLER_CODE = 1L - 1L;

    private static final String DEFAULT_BLOCK = "AAAAAAAAAA";
    private static final String UPDATED_BLOCK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_VISIBLE = false;
    private static final Boolean UPDATED_IS_VISIBLE = true;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/libraries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private LibraryMapper libraryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLibraryMockMvc;

    private Library library;

    private Library insertedLibrary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Library createEntity() {
        return new Library()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .block(DEFAULT_BLOCK)
            .isVisible(DEFAULT_IS_VISIBLE)
            .isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Library createUpdatedEntity() {
        return new Library()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .block(UPDATED_BLOCK)
            .isVisible(UPDATED_IS_VISIBLE)
            .isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    public void initTest() {
        library = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedLibrary != null) {
            libraryRepository.delete(insertedLibrary);
            insertedLibrary = null;
        }
    }

    @Test
    @Transactional
    void createLibrary() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Library
        LibraryDTO libraryDTO = libraryMapper.toDto(library);
        var returnedLibraryDTO = om.readValue(
            restLibraryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libraryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LibraryDTO.class
        );

        // Validate the Library in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLibrary = libraryMapper.toEntity(returnedLibraryDTO);
        assertLibraryUpdatableFieldsEquals(returnedLibrary, getPersistedLibrary(returnedLibrary));

        insertedLibrary = returnedLibrary;
    }

    @Test
    @Transactional
    void createLibraryWithExistingId() throws Exception {
        // Create the Library with an existing ID
        library.setId(1L);
        LibraryDTO libraryDTO = libraryMapper.toDto(library);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLibraryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libraryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Library in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLibraries() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList
        restLibraryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(library.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.intValue())))
            .andExpect(jsonPath("$.[*].block").value(hasItem(DEFAULT_BLOCK)))
            .andExpect(jsonPath("$.[*].isVisible").value(hasItem(DEFAULT_IS_VISIBLE.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getLibrary() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get the library
        restLibraryMockMvc
            .perform(get(ENTITY_API_URL_ID, library.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(library.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.intValue()))
            .andExpect(jsonPath("$.block").value(DEFAULT_BLOCK))
            .andExpect(jsonPath("$.isVisible").value(DEFAULT_IS_VISIBLE.booleanValue()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getLibrariesByIdFiltering() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        Long id = library.getId();

        defaultLibraryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLibraryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLibraryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLibrariesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where name equals to
        defaultLibraryFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLibrariesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where name in
        defaultLibraryFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLibrariesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where name is not null
        defaultLibraryFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllLibrariesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where name contains
        defaultLibraryFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLibrariesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where name does not contain
        defaultLibraryFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllLibrariesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where code equals to
        defaultLibraryFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLibrariesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where code in
        defaultLibraryFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLibrariesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where code is not null
        defaultLibraryFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllLibrariesByCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where code is greater than or equal to
        defaultLibraryFiltering("code.greaterThanOrEqual=" + DEFAULT_CODE, "code.greaterThanOrEqual=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLibrariesByCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where code is less than or equal to
        defaultLibraryFiltering("code.lessThanOrEqual=" + DEFAULT_CODE, "code.lessThanOrEqual=" + SMALLER_CODE);
    }

    @Test
    @Transactional
    void getAllLibrariesByCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where code is less than
        defaultLibraryFiltering("code.lessThan=" + UPDATED_CODE, "code.lessThan=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllLibrariesByCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where code is greater than
        defaultLibraryFiltering("code.greaterThan=" + SMALLER_CODE, "code.greaterThan=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllLibrariesByBlockIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where block equals to
        defaultLibraryFiltering("block.equals=" + DEFAULT_BLOCK, "block.equals=" + UPDATED_BLOCK);
    }

    @Test
    @Transactional
    void getAllLibrariesByBlockIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where block in
        defaultLibraryFiltering("block.in=" + DEFAULT_BLOCK + "," + UPDATED_BLOCK, "block.in=" + UPDATED_BLOCK);
    }

    @Test
    @Transactional
    void getAllLibrariesByBlockIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where block is not null
        defaultLibraryFiltering("block.specified=true", "block.specified=false");
    }

    @Test
    @Transactional
    void getAllLibrariesByBlockContainsSomething() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where block contains
        defaultLibraryFiltering("block.contains=" + DEFAULT_BLOCK, "block.contains=" + UPDATED_BLOCK);
    }

    @Test
    @Transactional
    void getAllLibrariesByBlockNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where block does not contain
        defaultLibraryFiltering("block.doesNotContain=" + UPDATED_BLOCK, "block.doesNotContain=" + DEFAULT_BLOCK);
    }

    @Test
    @Transactional
    void getAllLibrariesByIsVisibleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where isVisible equals to
        defaultLibraryFiltering("isVisible.equals=" + DEFAULT_IS_VISIBLE, "isVisible.equals=" + UPDATED_IS_VISIBLE);
    }

    @Test
    @Transactional
    void getAllLibrariesByIsVisibleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where isVisible in
        defaultLibraryFiltering("isVisible.in=" + DEFAULT_IS_VISIBLE + "," + UPDATED_IS_VISIBLE, "isVisible.in=" + UPDATED_IS_VISIBLE);
    }

    @Test
    @Transactional
    void getAllLibrariesByIsVisibleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where isVisible is not null
        defaultLibraryFiltering("isVisible.specified=true", "isVisible.specified=false");
    }

    @Test
    @Transactional
    void getAllLibrariesByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where isDeleted equals to
        defaultLibraryFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllLibrariesByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where isDeleted in
        defaultLibraryFiltering("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED, "isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllLibrariesByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        // Get all the libraryList where isDeleted is not null
        defaultLibraryFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    private void defaultLibraryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLibraryShouldBeFound(shouldBeFound);
        defaultLibraryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLibraryShouldBeFound(String filter) throws Exception {
        restLibraryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(library.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.intValue())))
            .andExpect(jsonPath("$.[*].block").value(hasItem(DEFAULT_BLOCK)))
            .andExpect(jsonPath("$.[*].isVisible").value(hasItem(DEFAULT_IS_VISIBLE.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restLibraryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLibraryShouldNotBeFound(String filter) throws Exception {
        restLibraryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLibraryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLibrary() throws Exception {
        // Get the library
        restLibraryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLibrary() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the library
        Library updatedLibrary = libraryRepository.findById(library.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLibrary are not directly saved in db
        em.detach(updatedLibrary);
        updatedLibrary
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .block(UPDATED_BLOCK)
            .isVisible(UPDATED_IS_VISIBLE)
            .isDeleted(UPDATED_IS_DELETED);
        LibraryDTO libraryDTO = libraryMapper.toDto(updatedLibrary);

        restLibraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, libraryDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libraryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Library in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLibraryToMatchAllProperties(updatedLibrary);
    }

    @Test
    @Transactional
    void putNonExistingLibrary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        library.setId(longCount.incrementAndGet());

        // Create the Library
        LibraryDTO libraryDTO = libraryMapper.toDto(library);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, libraryDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libraryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Library in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLibrary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        library.setId(longCount.incrementAndGet());

        // Create the Library
        LibraryDTO libraryDTO = libraryMapper.toDto(library);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibraryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(libraryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Library in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLibrary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        library.setId(longCount.incrementAndGet());

        // Create the Library
        LibraryDTO libraryDTO = libraryMapper.toDto(library);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibraryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(libraryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Library in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLibraryWithPatch() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the library using partial update
        Library partialUpdatedLibrary = new Library();
        partialUpdatedLibrary.setId(library.getId());

        partialUpdatedLibrary.code(UPDATED_CODE);

        restLibraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLibrary.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLibrary))
            )
            .andExpect(status().isOk());

        // Validate the Library in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLibraryUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLibrary, library), getPersistedLibrary(library));
    }

    @Test
    @Transactional
    void fullUpdateLibraryWithPatch() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the library using partial update
        Library partialUpdatedLibrary = new Library();
        partialUpdatedLibrary.setId(library.getId());

        partialUpdatedLibrary
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .block(UPDATED_BLOCK)
            .isVisible(UPDATED_IS_VISIBLE)
            .isDeleted(UPDATED_IS_DELETED);

        restLibraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLibrary.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLibrary))
            )
            .andExpect(status().isOk());

        // Validate the Library in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLibraryUpdatableFieldsEquals(partialUpdatedLibrary, getPersistedLibrary(partialUpdatedLibrary));
    }

    @Test
    @Transactional
    void patchNonExistingLibrary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        library.setId(longCount.incrementAndGet());

        // Create the Library
        LibraryDTO libraryDTO = libraryMapper.toDto(library);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, libraryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(libraryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Library in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLibrary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        library.setId(longCount.incrementAndGet());

        // Create the Library
        LibraryDTO libraryDTO = libraryMapper.toDto(library);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibraryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(libraryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Library in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLibrary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        library.setId(longCount.incrementAndGet());

        // Create the Library
        LibraryDTO libraryDTO = libraryMapper.toDto(library);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLibraryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(libraryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Library in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLibrary() throws Exception {
        // Initialize the database
        insertedLibrary = libraryRepository.saveAndFlush(library);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the library
        restLibraryMockMvc
            .perform(delete(ENTITY_API_URL_ID, library.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return libraryRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Library getPersistedLibrary(Library library) {
        return libraryRepository.findById(library.getId()).orElseThrow();
    }

    protected void assertPersistedLibraryToMatchAllProperties(Library expectedLibrary) {
        assertLibraryAllPropertiesEquals(expectedLibrary, getPersistedLibrary(expectedLibrary));
    }

    protected void assertPersistedLibraryToMatchUpdatableProperties(Library expectedLibrary) {
        assertLibraryAllUpdatablePropertiesEquals(expectedLibrary, getPersistedLibrary(expectedLibrary));
    }
}
