# swagger-android-client

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-android-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "io.swagger:swagger-android-client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/swagger-android-client-1.0.0.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import io.swagger.client.api.DefaultApi;

public class DefaultApiExample {

    public static void main(String[] args) {
        DefaultApi apiInstance = new DefaultApi();
        String boardId = "boardId_example"; // String | 版块ID
        String topicCategoryId = "topicCategoryId_example"; // String | 主题分类ID
        String topicOrder = "topicOrder_example"; // String | 主题排序方式 - 格式：field order 允许的字段名lastpost dateline，例如dateline desc, lastpost asc
        String pageNum = "pageNum_example"; // String | 版块主题页码
        String pageSize = "pageSize_example"; // String | 版块主题每页记录数
        try {
            ForumBoardDetailModel result = apiInstance.v10ForumBoardBoardIdDetailGet(boardId, topicCategoryId, topicOrder, pageNum, pageSize);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#v10ForumBoardBoardIdDetailGet");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *http://appv6.55haitao.cm/*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*DefaultApi* | [**v10ForumBoardBoardIdDetailGet**](docs/DefaultApi.md#v10ForumBoardBoardIdDetailGet) | **GET** /v1.0/forum/board/{board_id}/detail | 
*DefaultApi* | [**v10ForumBoardBoardIdTopicsListGet**](docs/DefaultApi.md#v10ForumBoardBoardIdTopicsListGet) | **GET** /v1.0/forum/board/{board_id}/topics_list | 
*DefaultApi* | [**v10ForumBoardsGet**](docs/DefaultApi.md#v10ForumBoardsGet) | **GET** /v1.0/forum/boards | 
*DefaultApi* | [**v10ForumIndexFollowedGet**](docs/DefaultApi.md#v10ForumIndexFollowedGet) | **GET** /v1.0/forum/index/followed | 
*DefaultApi* | [**v10ForumIndexHotTopicsGet**](docs/DefaultApi.md#v10ForumIndexHotTopicsGet) | **GET** /v1.0/forum/index/hot_topics | 
*DefaultApi* | [**v10ForumIndexShowOrdersGet**](docs/DefaultApi.md#v10ForumIndexShowOrdersGet) | **GET** /v1.0/forum/index/show_orders | 
*DefaultApi* | [**v10ForumTalentExplaintionPost**](docs/DefaultApi.md#v10ForumTalentExplaintionPost) | **POST** /v1.0/forum/talent_explaintion | 
*DefaultApi* | [**v10ForumTalentsGet**](docs/DefaultApi.md#v10ForumTalentsGet) | **GET** /v1.0/forum/talents | 
*DefaultApi* | [**v10ForumTalentsIndexGet**](docs/DefaultApi.md#v10ForumTalentsIndexGet) | **GET** /v1.0/forum/talents/index | 
*DefaultApi* | [**v10ForumTalentsListGet**](docs/DefaultApi.md#v10ForumTalentsListGet) | **GET** /v1.0/forum/talents/list | 
*DefaultApi* | [**v10ForumTalkTopicsListGet**](docs/DefaultApi.md#v10ForumTalkTopicsListGet) | **GET** /v1.0/forum/talk_topics_list | 
*DefaultApi* | [**v10ForumTopicPost**](docs/DefaultApi.md#v10ForumTopicPost) | **POST** /v1.0/forum/topic | 
*DefaultApi* | [**v10ForumTopicTopicIdGet**](docs/DefaultApi.md#v10ForumTopicTopicIdGet) | **GET** /v1.0/forum/topic/{topic_id} | 
*DefaultApi* | [**v10ForumTopicTopicIdReplyPost**](docs/DefaultApi.md#v10ForumTopicTopicIdReplyPost) | **POST** /v1.0/forum/topic/{topic_id}/reply | 
*DefaultApi* | [**v10ForumTopicsListGet**](docs/DefaultApi.md#v10ForumTopicsListGet) | **GET** /v1.0/forum/topics_list | 
*DefaultApi* | [**v10ForumTransshipperTransshipperIdForumTopicsListGet**](docs/DefaultApi.md#v10ForumTransshipperTransshipperIdForumTopicsListGet) | **GET** /v1.0/forum/transshipper/{transshipper_id}/forum_topics_list | 
*DefaultApi* | [**v10SearchingKeywordsBoardsGet**](docs/DefaultApi.md#v10SearchingKeywordsBoardsGet) | **GET** /v1.0/searching/{keywords}/boards | 
*DefaultApi* | [**v10SearchingKeywordsForumBoardBoardIdTopicsListGet**](docs/DefaultApi.md#v10SearchingKeywordsForumBoardBoardIdTopicsListGet) | **GET** /v1.0/searching/{keywords}/forum/board/{board_id}/topics_list | 
*DefaultApi* | [**v10SearchingKeywordsTagsListGet**](docs/DefaultApi.md#v10SearchingKeywordsTagsListGet) | **GET** /v1.0/searching/{keywords}/tags_list | 
*DefaultApi* | [**v10StoreStoreIdForumTopicsListGet**](docs/DefaultApi.md#v10StoreStoreIdForumTopicsListGet) | **GET** /v1.0/store/{store_id}/forum_topics_list | 
*DefaultApi* | [**v10TagTagIdDealsListGet**](docs/DefaultApi.md#v10TagTagIdDealsListGet) | **GET** /v1.0/tag/{tag_id}/deals_list | 
*DefaultApi* | [**v10TagTagIdTopicsListGet**](docs/DefaultApi.md#v10TagTagIdTopicsListGet) | **GET** /v1.0/tag/{tag_id}/topics_list | 
*DefaultApi* | [**v10TagsListGet**](docs/DefaultApi.md#v10TagsListGet) | **GET** /v1.0/tags/list | 
*DefaultApi* | [**v10UserCollectionDelete**](docs/DefaultApi.md#v10UserCollectionDelete) | **DELETE** /v1.0/user/collection | 
*DefaultApi* | [**v10UserCollectionForumBoardsListGet**](docs/DefaultApi.md#v10UserCollectionForumBoardsListGet) | **GET** /v1.0/user/collection/forum_boards_list | 
*DefaultApi* | [**v10UserCollectionForumTopicsListGet**](docs/DefaultApi.md#v10UserCollectionForumTopicsListGet) | **GET** /v1.0/user/collection/forum_topics_list | 
*DefaultApi* | [**v10UserCollectionPost**](docs/DefaultApi.md#v10UserCollectionPost) | **POST** /v1.0/user/collection | 
*DefaultApi* | [**v10UserFriendsForumTopicsListGet**](docs/DefaultApi.md#v10UserFriendsForumTopicsListGet) | **GET** /v1.0/user/friends_forum_topics_list | 
*DefaultApi* | [**v10UserPraisingDelete**](docs/DefaultApi.md#v10UserPraisingDelete) | **DELETE** /v1.0/user/praising | 
*DefaultApi* | [**v10UserPraisingPost**](docs/DefaultApi.md#v10UserPraisingPost) | **POST** /v1.0/user/praising | 
*DefaultApi* | [**v10UserUidForumTopicsListGet**](docs/DefaultApi.md#v10UserUidForumTopicsListGet) | **GET** /v1.0/user/{uid}/forum_topics_list | 


## Documentation for Models

 - [ErrorModel](docs/ErrorModel.md)
 - [ForumBoardDetailModel](docs/ForumBoardDetailModel.md)
 - [ForumBoardDetailModelData](docs/ForumBoardDetailModelData.md)
 - [ForumBoardDetailModelDataCategories](docs/ForumBoardDetailModelDataCategories.md)
 - [ForumBoardDetailModelDataTopTopics](docs/ForumBoardDetailModelDataTopTopics.md)
 - [ForumBoardDetailModelDataTopics](docs/ForumBoardDetailModelDataTopics.md)
 - [ForumBoardDetailModelDataTopicsRows](docs/ForumBoardDetailModelDataTopicsRows.md)
 - [ForumBoardTopicsListModel](docs/ForumBoardTopicsListModel.md)
 - [ForumBoardsModel](docs/ForumBoardsModel.md)
 - [ForumBoardsModelBoards](docs/ForumBoardsModelBoards.md)
 - [ForumBoardsModelData](docs/ForumBoardsModelData.md)
 - [ForumIndexFollowedModel](docs/ForumIndexFollowedModel.md)
 - [ForumIndexFollowedModelData](docs/ForumIndexFollowedModelData.md)
 - [ForumIndexFollowedModelDataTalents](docs/ForumIndexFollowedModelDataTalents.md)
 - [ForumIndexHotTopicsModel](docs/ForumIndexHotTopicsModel.md)
 - [ForumIndexHotTopicsModelData](docs/ForumIndexHotTopicsModelData.md)
 - [ForumIndexHotTopicsModelDataBoards](docs/ForumIndexHotTopicsModelDataBoards.md)
 - [ForumIndexHotTopicsModelDataSlidingPics](docs/ForumIndexHotTopicsModelDataSlidingPics.md)
 - [ForumIndexHotTopicsModelDataTalents](docs/ForumIndexHotTopicsModelDataTalents.md)
 - [ForumIndexHotTopicsModelDataTalentsTopics](docs/ForumIndexHotTopicsModelDataTalentsTopics.md)
 - [ForumIndexShowOrdersModel](docs/ForumIndexShowOrdersModel.md)
 - [ForumIndexShowOrdersModelData](docs/ForumIndexShowOrdersModelData.md)
 - [ForumIndexShowOrdersModelDataCommendedTopics](docs/ForumIndexShowOrdersModelDataCommendedTopics.md)
 - [ForumTalentsListModel](docs/ForumTalentsListModel.md)
 - [ForumTalentsListModelData](docs/ForumTalentsListModelData.md)
 - [ForumTalentsModel](docs/ForumTalentsModel.md)
 - [ForumTopicModel](docs/ForumTopicModel.md)
 - [ForumTopicModelData](docs/ForumTopicModelData.md)
 - [ForumTopicModelDataReplies](docs/ForumTopicModelDataReplies.md)
 - [ForumTopicsListModel](docs/ForumTopicsListModel.md)
 - [FriendsForumTopicsListModel](docs/FriendsForumTopicsListModel.md)
 - [OptSuccessModel](docs/OptSuccessModel.md)
 - [SearchingBoardsModel](docs/SearchingBoardsModel.md)
 - [SearchingForumBoardTopicsListModel](docs/SearchingForumBoardTopicsListModel.md)
 - [SearchingForumBoardTopicsListModelData](docs/SearchingForumBoardTopicsListModelData.md)
 - [SearchingTagsListModel](docs/SearchingTagsListModel.md)
 - [StoreForumTopicsListModel](docs/StoreForumTopicsListModel.md)
 - [TagDealsListModel](docs/TagDealsListModel.md)
 - [TagDealsListModelData](docs/TagDealsListModelData.md)
 - [TagDealsListModelDataRows](docs/TagDealsListModelDataRows.md)
 - [TagTopicsListModel](docs/TagTopicsListModel.md)
 - [TagTopicsListModelData](docs/TagTopicsListModelData.md)
 - [TagTopicsListModelDataRows](docs/TagTopicsListModelDataRows.md)
 - [TagTopicsListModelDataTags](docs/TagTopicsListModelDataTags.md)
 - [TagsListModel](docs/TagsListModel.md)
 - [TagsListModelData](docs/TagsListModelData.md)
 - [TagsListModelDataRows](docs/TagsListModelDataRows.md)
 - [TalentExplaintionModel](docs/TalentExplaintionModel.md)
 - [TalentsIndexModel](docs/TalentsIndexModel.md)
 - [TalentsIndexModelData](docs/TalentsIndexModelData.md)
 - [TalentsIndexModelDataNextTalent](docs/TalentsIndexModelDataNextTalent.md)
 - [TalentsIndexModelDataSlidingPics](docs/TalentsIndexModelDataSlidingPics.md)
 - [TalentsIndexModelDataTalentsCategories](docs/TalentsIndexModelDataTalentsCategories.md)
 - [TalkTopicsListModel](docs/TalkTopicsListModel.md)
 - [TransshipperForumTopicsListModel](docs/TransshipperForumTopicsListModel.md)
 - [TransshipperForumTopicsListModelData](docs/TransshipperForumTopicsListModelData.md)
 - [TransshipperForumTopicsListModelDataRows](docs/TransshipperForumTopicsListModelDataRows.md)
 - [UserCollectionForumBoardsListModel](docs/UserCollectionForumBoardsListModel.md)
 - [UserCollectionForumBoardsListModelData](docs/UserCollectionForumBoardsListModelData.md)
 - [UserCollectionForumTopicsListModel](docs/UserCollectionForumTopicsListModel.md)
 - [UserForumTopicsListModel](docs/UserForumTopicsListModel.md)


## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author



