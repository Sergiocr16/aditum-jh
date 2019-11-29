package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Article;
import com.lighthouse.aditum.domain.Regulation;
import com.lighthouse.aditum.repository.ArticleRepository;
import com.lighthouse.aditum.service.dto.ArticleDTO;
import com.lighthouse.aditum.service.dto.CategoriesKeyWordsQueryDTO;
import com.lighthouse.aditum.service.dto.ChapterDTO;
import com.lighthouse.aditum.service.dto.RegulationDTO;
import com.lighthouse.aditum.service.mapper.ArticleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
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

    private final ChapterService chapterService;

    private final RegulationService regulationService;

    public ArticleService(@Lazy RegulationService regulationService,@Lazy  ChapterService chapterService, SubsectionService subsectionService, ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.subsectionService = subsectionService;
        this.chapterService = chapterService;
        this.regulationService = regulationService;
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
    public Page<ArticleDTO> findAll(Pageable pageable, Long chapterId) {
        log.debug("Request to get all Articles");
        Page<ArticleDTO> articleDTOS = articleRepository.findByChapterIdAndDeleted(pageable, chapterId, 0).map(articleMapper::toDto);
        for (ArticleDTO articleDTO : articleDTOS) {
            for (ArticleDTO reference : articleDTO.getReferences()) {
                ChapterDTO chapterDTO = chapterService.findOne(reference.getChapterId());
                reference.setRegulationName(regulationService.findOne(chapterDTO.getRegulationId()).getName());
                reference.setChapterName(chapterDTO.getName());
                reference.setChapterDescription(chapterDTO.getDescription());
                reference.setSubsections(subsectionService.getCompleteSubsectionsByArticle(reference.getId()));;
            }
        }
        return articleDTOS;
    }


    @Transactional(readOnly = true)
    public List<ArticleDTO> getCompleteArticlesByChapter(Long chapterId) {
        log.debug("Request to get all Articles");
        List<Article> articles = articleRepository.findByChapterIdAndDeleted(chapterId, 0);
        List<ArticleDTO> articleDTOS = new ArrayList<>();
        for (Article article : articles) {
            ArticleDTO articleDTO = articleMapper.toDto(article);
            articleDTO.setSubsections(subsectionService.getCompleteSubsectionsByArticle(article.getId()));
            articleDTOS.add(articleDTO);
        }

        return articleDTOS;
    }


    @Transactional(readOnly = true)
    public RegulationDTO findByCategoriesAndKeyWords(CategoriesKeyWordsQueryDTO categoriesKeyWordsQueryDTO) {
        log.debug("Request to get all Articles");
        List<ArticleDTO> articleDTOS = new ArrayList<>();
        List<ChapterDTO> chapterDTOS = new ArrayList<>();
        List<Article> articles1 = new ArrayList<>();
        List<Article> articles2 = new ArrayList<>();
        if(categoriesKeyWordsQueryDTO.getCategories().size()>0){
            articles1 = articleRepository.findArticlesByCategories(categoriesKeyWordsQueryDTO.getCategories());
        }
        if(categoriesKeyWordsQueryDTO.getKeyWords().size()>0){
            articles2 = articleRepository.findArticlesByKeyWords(categoriesKeyWordsQueryDTO.getKeyWords());
        }

        for (Article article : articles2) {
            if (articles1.indexOf(article) >= 0) {
            } else {
                articles1.add(article);
            }
        }

        for (Article article : articles1) {
            ArticleDTO articleDTO = articleMapper.toDto(article);
            articleDTO.setSubsections(subsectionService.getCompleteSubsectionsByArticle(article.getId()));
            articleDTOS.add(articleDTO);
            ChapterDTO chapterDTO =  getChaptersAndRegulationFromArticles(articleDTO);
            int index = chapterDTOS.indexOf(chapterDTO);
            if(chapterDTO.getRegulationId()==categoriesKeyWordsQueryDTO.getRegulationDTO().getId()){
                if (index >= 0) {
                    chapterDTOS.get(index).getArticles().add(articleDTO);
                } else {
                    chapterDTO.getArticles().add(articleDTO);
                    chapterDTOS.add(chapterDTO);
                }
            }


        }
        RegulationDTO regulationDTO = regulationService.findOne(categoriesKeyWordsQueryDTO.getRegulationDTO().getId());
        regulationDTO.setChapters(chapterDTOS);
        for (ChapterDTO chapter : regulationDTO.getChapters()) {
            for (ArticleDTO articleDTO : chapter.getArticles()) {
                for (ArticleDTO reference : articleDTO.getReferences()) {
                    ChapterDTO chapterDTO = chapterService.findOne(reference.getChapterId());
                    reference.setRegulationName(regulationService.findOne(chapterDTO.getRegulationId()).getName());
                    reference.setChapterName(chapterDTO.getName());
                    reference.setChapterDescription(chapterDTO.getDescription());
                    reference.setSubsections(subsectionService.getCompleteSubsectionsByArticle(reference.getId()));;
                }
            }
        }
        return regulationDTO;
    }

    public ChapterDTO getChaptersAndRegulationFromArticles(ArticleDTO article){
        return chapterService.findOne(article.getChapterId());
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
        for (ArticleDTO reference:
             articleDTO.getReferences()) {
            reference.setRegulationIdReference(chapterService.findOne( reference.getChapterId()).getRegulationId());
        }
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
