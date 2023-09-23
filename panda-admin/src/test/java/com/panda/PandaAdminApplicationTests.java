package com.panda;

import com.panda.system.domin.SysMovie;
import com.panda.system.domin.vo.SysMovieVo;
import com.panda.system.service.EsService;
import com.panda.system.service.SysMovieService;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;


import javax.sql.DataSource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;

@SpringBootTest
@Slf4j
class PandaAdminApplicationTests {

    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() throws SQLException {
        System.out.println(dataSource.getClass());
        System.out.println(dataSource.getConnection());
    }

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private SysMovieService sysMovieService;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private EsService esService;

    @Test
    public void testEs() {
        List<SysMovie> list = esService.searchSysMovieLike("");
        for (SysMovie sysMovie : list) {
            System.out.println(sysMovie);
        }

    }

    @Test
    void redisTest(){
        for (Object comment6 : Objects.requireNonNull(redisTemplate.opsForZSet().range("comment6", 0, -1))) {
            System.out.println(comment6);
        }
        for (Object domestic : redisTemplate.opsForZSet().range("domestic", 0, -1)) {
            System.out.println(domestic);
        }

    }

    //使用ElasticsearchOperations创建索引
    @Test
    public void run() {
        IndexCoordinates indexCoordinates = IndexCoordinates.of("movie");
        IndexOperations indexOperations = elasticsearchOperations.indexOps(indexCoordinates);
        if (!indexOperations.exists()) {
            // 创建索引
            indexOperations.create();
            indexOperations.refresh();
            // 将映射关系写入到索引，即将数据结构和类型写入到索引
            indexOperations.putMapping(SysMovie.class);
            indexOperations.refresh();
            log.info(">>>> 创建索引和映射关系成功 <<<<");
        }
    }

    @Test
    public void insertMovieToDoc(){
        List<SysMovie> allMovies = sysMovieService.findAllMovies(new SysMovieVo());
        System.out.println(allMovies.size());
        for (SysMovie allMovie : allMovies) {
            elasticsearchOperations.save(allMovie);
        }

    }

    @Test
    public void searchMovie(){
        Criteria criteria = new Criteria("movieName").in("送你一朵电锯");
        Query query = new CriteriaQuery(criteria);

//        Criteria criteria = new Criteria("price").greaterThan(42.0).lessThan(34.0L);
//        Query query = new CriteriaQuery(criteria);

//        Criteria criteria = new Criteria("lastname").is("Miller")
//                .and("firstname").is("James")
//        Query query = new CriteriaQuery(criteria);

//        Query query = NativeQuery.builder()
//                .withAggregation("lastNames", Aggregation.of(a -> a
//                        .terms(ta -> ta.field("last-name").size(10))))
//                .withQuery(q -> q
//                        .match(m -> m
//                                .field("firstName")
//                                .query(firstName)
//                        )
//                )
//                .withPageable(pageable)
//                .build();
        org.springframework.data.elasticsearch.core.SearchHits<SysMovie> search = elasticsearchOperations.search(query, SysMovie.class);
        for (org.springframework.data.elasticsearch.core.SearchHit<SysMovie> sysMovieSearchHit : search) {
            System.out.println(sysMovieSearchHit.getContent());
        }
    }

    //使用RestHighLevelClient创建索引
    @Test
    public void createIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("movie");
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
        restHighLevelClient.close();
    }

    @Test
    void testBulkAddMovie() throws IOException {
        List<SysMovie> allMovies = sysMovieService.findAllMovies(new SysMovieVo());
        BulkRequest bulkRequest = new BulkRequest();
        int id = 1;
        //批量处理请求
        for (SysMovie allMovie : allMovies) {
            bulkRequest.add(new IndexRequest().index("movie").id("" + id++).source(JSONValue.toJSONString(allMovie), XContentType.JSON));
        }
        BulkResponse bulkItemResponses = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulkItemResponses.hasFailures());
    }

    @Test
    void search() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("movie");
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        //模糊查询
        builder.query(QueryBuilders.matchQuery("movieName", "之"));
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        List list = new ArrayList();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
            list.add(hit.getSourceAsString());
        }

        System.out.println(hits);
    }



    @Test
    public void getDataByRedis() {
        long l = System.currentTimeMillis();
        Set<SysMovie> total = redisTemplate.boundZSetOps("total").range(0L, 10L);
        System.out.println(redisTemplate.boundZSetOps("total").size());
        for (SysMovie sysMovie : total) {
            System.out.println(sysMovie);
            if (sysMovie.getMovieId() == 6) {
                redisTemplate.opsForZSet().remove("total", sysMovie);
                sysMovie.setWatchNum(sysMovie.getWatchNum() + 1);
                redisTemplate.opsForZSet().add("total", sysMovie, sysMovie.getWatchNum());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - l);
    }

    @Autowired
    private MinioClient minioClient;

    /**
     * 获取单个桶中的所有文件
     */
    @Test
    void getBucketObjectName() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<String> list= new ArrayList<>();
        Iterable<Result<Item>> video = minioClient.listObjects(ListObjectsArgs.builder().bucket("video").build());
        for (Result<Item> itemResult : video) {
            Item item = itemResult.get();
            list.add(item.objectName());
        }
        System.out.println(list);
    }

}
