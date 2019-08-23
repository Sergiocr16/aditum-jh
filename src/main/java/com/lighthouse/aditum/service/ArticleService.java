package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Article;
import com.lighthouse.aditum.repository.ArticleRepository;
import com.lighthouse.aditum.service.dto.ArticleDTO;
import com.lighthouse.aditum.service.mapper.ArticleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * Service Implementation for managing Article.
 */
@Service
@Transactional
public class ArticleService {

    private final Logger log = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;

    private final SubsectionService subsectionService;


    public ArticleService(SubsectionService subsectionService,ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.subsectionService = subsectionService;
    }

    /**
     * Save a article.
     *
     * @param articleDTO the entity to save
     * @return the persisted entity
     */
    public ArticleDTO save(ArticleDTO articleDTO) {
        log.debug("Request to save Article : {}", articleDTO);
        Article article = articleMapper.toEntity(articleDTO);
        article = articleRepository.save(article);
        return articleMapper.toDto(article);
    }

    /**
     * Get all the articles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ArticleDTO> findAll(Pageable pageable,Long chapterId) {
        log.debug("Request to get all Articles");
        return articleRepository.findByChapterIdAndDeleted(pageable,chapterId,0)
            .map(articleMapper::toDto);
    }


    @Transactional(readOnly = true)
    public List<ArticleDTO> getCompleteArticlesByChapter(Long chapterId) {
        log.debug("Request to get all Articles");
        List<Article> articles = articleRepository.findByChapterIdAndDeleted(chapterId,0);
        List<ArticleDTO> articleDTOS = new ArrayList<>();
        for (Article article: articles) {
            ArticleDTO articleDTO = articleMapper.toDto(article);
            articleDTO.setSubsections(subsectionService.getCompleteSubsectionsByArticle(article.getId()));
            articleDTOS.add(articleDTO);
        }

        return articleDTOS;
    }

    /**
     * Get one article by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ArticleDTO findOne(Long id) {
        log.debug("Request to get Article : {}", id);
        Article article = articleRepository.findOneWithEagerRelationships(id);
        String a = "a";
        ArticleDTO articleDTO = articleMapper.toDto(article);
        String ads = "a";
        return articleDTO;
    }

    /**
     * Delete the article by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Article : {}", id);
        articleRepository.delete(id);
    }
}
