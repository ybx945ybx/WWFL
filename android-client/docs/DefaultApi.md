# DefaultApi

All URIs are relative to *http://appv6.55haitao.cm/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**v10ForumBoardBoardIdDetailGet**](DefaultApi.md#v10ForumBoardBoardIdDetailGet) | **GET** /v1.0/forum/board/{board_id}/detail | 
[**v10ForumBoardBoardIdTopicsListGet**](DefaultApi.md#v10ForumBoardBoardIdTopicsListGet) | **GET** /v1.0/forum/board/{board_id}/topics_list | 
[**v10ForumBoardsGet**](DefaultApi.md#v10ForumBoardsGet) | **GET** /v1.0/forum/boards | 
[**v10ForumIndexFollowedGet**](DefaultApi.md#v10ForumIndexFollowedGet) | **GET** /v1.0/forum/index/followed | 
[**v10ForumIndexHotTopicsGet**](DefaultApi.md#v10ForumIndexHotTopicsGet) | **GET** /v1.0/forum/index/hot_topics | 
[**v10ForumIndexShowOrdersGet**](DefaultApi.md#v10ForumIndexShowOrdersGet) | **GET** /v1.0/forum/index/show_orders | 
[**v10ForumTalentExplaintionPost**](DefaultApi.md#v10ForumTalentExplaintionPost) | **POST** /v1.0/forum/talent_explaintion | 
[**v10ForumTalentsGet**](DefaultApi.md#v10ForumTalentsGet) | **GET** /v1.0/forum/talents | 
[**v10ForumTalentsIndexGet**](DefaultApi.md#v10ForumTalentsIndexGet) | **GET** /v1.0/forum/talents/index | 
[**v10ForumTalentsListGet**](DefaultApi.md#v10ForumTalentsListGet) | **GET** /v1.0/forum/talents/list | 
[**v10ForumTalkTopicsListGet**](DefaultApi.md#v10ForumTalkTopicsListGet) | **GET** /v1.0/forum/talk_topics_list | 
[**v10ForumTopicPost**](DefaultApi.md#v10ForumTopicPost) | **POST** /v1.0/forum/topic | 
[**v10ForumTopicTopicIdGet**](DefaultApi.md#v10ForumTopicTopicIdGet) | **GET** /v1.0/forum/topic/{topic_id} | 
[**v10ForumTopicTopicIdReplyPost**](DefaultApi.md#v10ForumTopicTopicIdReplyPost) | **POST** /v1.0/forum/topic/{topic_id}/reply | 
[**v10ForumTopicsListGet**](DefaultApi.md#v10ForumTopicsListGet) | **GET** /v1.0/forum/topics_list | 
[**v10ForumTransshipperTransshipperIdForumTopicsListGet**](DefaultApi.md#v10ForumTransshipperTransshipperIdForumTopicsListGet) | **GET** /v1.0/forum/transshipper/{transshipper_id}/forum_topics_list | 
[**v10SearchingKeywordsBoardsGet**](DefaultApi.md#v10SearchingKeywordsBoardsGet) | **GET** /v1.0/searching/{keywords}/boards | 
[**v10SearchingKeywordsForumBoardBoardIdTopicsListGet**](DefaultApi.md#v10SearchingKeywordsForumBoardBoardIdTopicsListGet) | **GET** /v1.0/searching/{keywords}/forum/board/{board_id}/topics_list | 
[**v10SearchingKeywordsTagsListGet**](DefaultApi.md#v10SearchingKeywordsTagsListGet) | **GET** /v1.0/searching/{keywords}/tags_list | 
[**v10StoreStoreIdForumTopicsListGet**](DefaultApi.md#v10StoreStoreIdForumTopicsListGet) | **GET** /v1.0/store/{store_id}/forum_topics_list | 
[**v10TagTagIdDealsListGet**](DefaultApi.md#v10TagTagIdDealsListGet) | **GET** /v1.0/tag/{tag_id}/deals_list | 
[**v10TagTagIdTopicsListGet**](DefaultApi.md#v10TagTagIdTopicsListGet) | **GET** /v1.0/tag/{tag_id}/topics_list | 
[**v10TagsListGet**](DefaultApi.md#v10TagsListGet) | **GET** /v1.0/tags/list | 
[**v10UserCollectionDelete**](DefaultApi.md#v10UserCollectionDelete) | **DELETE** /v1.0/user/collection | 
[**v10UserCollectionForumBoardsListGet**](DefaultApi.md#v10UserCollectionForumBoardsListGet) | **GET** /v1.0/user/collection/forum_boards_list | 
[**v10UserCollectionForumTopicsListGet**](DefaultApi.md#v10UserCollectionForumTopicsListGet) | **GET** /v1.0/user/collection/forum_topics_list | 
[**v10UserCollectionPost**](DefaultApi.md#v10UserCollectionPost) | **POST** /v1.0/user/collection | 
[**v10UserFriendsForumTopicsListGet**](DefaultApi.md#v10UserFriendsForumTopicsListGet) | **GET** /v1.0/user/friends_forum_topics_list | 
[**v10UserPraisingDelete**](DefaultApi.md#v10UserPraisingDelete) | **DELETE** /v1.0/user/praising | 
[**v10UserPraisingPost**](DefaultApi.md#v10UserPraisingPost) | **POST** /v1.0/user/praising | 
[**v10UserUidForumTopicsListGet**](DefaultApi.md#v10UserUidForumTopicsListGet) | **GET** /v1.0/user/{uid}/forum_topics_list | 


<a name="v10ForumBoardBoardIdDetailGet"></a>
# **v10ForumBoardBoardIdDetailGet**
> ForumBoardDetailModel v10ForumBoardBoardIdDetailGet(boardId, topicCategoryId, topicOrder, pageNum, pageSize)



版块详情 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

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
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **boardId** | **String**| 版块ID |
 **topicCategoryId** | **String**| 主题分类ID | [optional]
 **topicOrder** | **String**| 主题排序方式 - 格式：field order 允许的字段名lastpost dateline，例如dateline desc, lastpost asc | [optional]
 **pageNum** | **String**| 版块主题页码 | [optional]
 **pageSize** | **String**| 版块主题每页记录数 | [optional]

### Return type

[**ForumBoardDetailModel**](ForumBoardDetailModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10ForumBoardBoardIdTopicsListGet"></a>
# **v10ForumBoardBoardIdTopicsListGet**
> ForumBoardTopicsListModel v10ForumBoardBoardIdTopicsListGet(boardId, topicTypeId, topicOrder, pageNum, pageSize)



版块详情 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String boardId = "boardId_example"; // String | 版块ID
String topicTypeId = "topicTypeId_example"; // String | 主题类型ID
String topicOrder = "topicOrder_example"; // String | 主题排序方式 - 格式：field order 允许的字段名lastpost dateline，例如dateline desc, lastpost asc
String pageNum = "pageNum_example"; // String | 版块主题页码
String pageSize = "pageSize_example"; // String | 版块主题每页记录数
try {
    ForumBoardTopicsListModel result = apiInstance.v10ForumBoardBoardIdTopicsListGet(boardId, topicTypeId, topicOrder, pageNum, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10ForumBoardBoardIdTopicsListGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **boardId** | **String**| 版块ID |
 **topicTypeId** | **String**| 主题类型ID | [optional]
 **topicOrder** | **String**| 主题排序方式 - 格式：field order 允许的字段名lastpost dateline，例如dateline desc, lastpost asc | [optional]
 **pageNum** | **String**| 版块主题页码 | [optional]
 **pageSize** | **String**| 版块主题每页记录数 | [optional]

### Return type

[**ForumBoardTopicsListModel**](ForumBoardTopicsListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10ForumBoardsGet"></a>
# **v10ForumBoardsGet**
> ForumBoardsModel v10ForumBoardsGet()



获取版块列表 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
try {
    ForumBoardsModel result = apiInstance.v10ForumBoardsGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10ForumBoardsGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ForumBoardsModel**](ForumBoardsModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10ForumIndexFollowedGet"></a>
# **v10ForumIndexFollowedGet**
> ForumIndexFollowedModel v10ForumIndexFollowedGet(boardsCount, talkTopicsCount, friendsPostsCount)



论坛首页 - 关注 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String boardsCount = "boardsCount_example"; // String | 显示版块数
String talkTopicsCount = "talkTopicsCount_example"; // String | 显示话题数
String friendsPostsCount = "friendsPostsCount_example"; // String | 显示好友动态数
try {
    ForumIndexFollowedModel result = apiInstance.v10ForumIndexFollowedGet(boardsCount, talkTopicsCount, friendsPostsCount);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10ForumIndexFollowedGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **boardsCount** | **String**| 显示版块数 | [optional]
 **talkTopicsCount** | **String**| 显示话题数 | [optional]
 **friendsPostsCount** | **String**| 显示好友动态数 | [optional]

### Return type

[**ForumIndexFollowedModel**](ForumIndexFollowedModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10ForumIndexHotTopicsGet"></a>
# **v10ForumIndexHotTopicsGet**
> ForumIndexHotTopicsModel v10ForumIndexHotTopicsGet(toptopicsCount, talentsCount, hottopicsCount)



论坛首页 - 热帖 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String toptopicsCount = "toptopicsCount_example"; // String | 置顶帖数
String talentsCount = "talentsCount_example"; // String | 显示达人数
String hottopicsCount = "hottopicsCount_example"; // String | 显示热帖数
try {
    ForumIndexHotTopicsModel result = apiInstance.v10ForumIndexHotTopicsGet(toptopicsCount, talentsCount, hottopicsCount);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10ForumIndexHotTopicsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **toptopicsCount** | **String**| 置顶帖数 | [optional]
 **talentsCount** | **String**| 显示达人数 | [optional]
 **hottopicsCount** | **String**| 显示热帖数 | [optional]

### Return type

[**ForumIndexHotTopicsModel**](ForumIndexHotTopicsModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10ForumIndexShowOrdersGet"></a>
# **v10ForumIndexShowOrdersGet**
> ForumIndexShowOrdersModel v10ForumIndexShowOrdersGet(tagsCount, topicsCount)



论坛首页 - 晒单帖 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String tagsCount = "tagsCount_example"; // String | 显示标签数
String topicsCount = "topicsCount_example"; // String | 显示帖子数
try {
    ForumIndexShowOrdersModel result = apiInstance.v10ForumIndexShowOrdersGet(tagsCount, topicsCount);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10ForumIndexShowOrdersGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **tagsCount** | **String**| 显示标签数 | [optional]
 **topicsCount** | **String**| 显示帖子数 | [optional]

### Return type

[**ForumIndexShowOrdersModel**](ForumIndexShowOrdersModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10ForumTalentExplaintionPost"></a>
# **v10ForumTalentExplaintionPost**
> TalentExplaintionModel v10ForumTalentExplaintionPost()



论坛 - 成为达人 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
try {
    TalentExplaintionModel result = apiInstance.v10ForumTalentExplaintionPost();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10ForumTalentExplaintionPost");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**TalentExplaintionModel**](TalentExplaintionModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10ForumTalentsGet"></a>
# **v10ForumTalentsGet**
> ForumTalentsModel v10ForumTalentsGet(count)



论坛 - 海淘达人 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String count = "count_example"; // String | 取多少条数据
try {
    ForumTalentsModel result = apiInstance.v10ForumTalentsGet(count);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10ForumTalentsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **count** | **String**| 取多少条数据 | [optional]

### Return type

[**ForumTalentsModel**](ForumTalentsModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10ForumTalentsIndexGet"></a>
# **v10ForumTalentsIndexGet**
> TalentsIndexModel v10ForumTalentsIndexGet(categoryId, categoriesCount, talentsCount)



论坛 - 达人首页 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String categoryId = "categoryId_example"; // String | 达人分类ID 不填取第一个
String categoriesCount = "categoriesCount_example"; // String | 取多少个达人分类
String talentsCount = "talentsCount_example"; // String | 取多少个达人
try {
    TalentsIndexModel result = apiInstance.v10ForumTalentsIndexGet(categoryId, categoriesCount, talentsCount);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10ForumTalentsIndexGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **categoryId** | **String**| 达人分类ID 不填取第一个 | [optional]
 **categoriesCount** | **String**| 取多少个达人分类 | [optional]
 **talentsCount** | **String**| 取多少个达人 | [optional]

### Return type

[**TalentsIndexModel**](TalentsIndexModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10ForumTalentsListGet"></a>
# **v10ForumTalentsListGet**
> ForumTalentsListModel v10ForumTalentsListGet(categoryId, pageNum, pageSize)



论坛 - 海淘达人列表 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String categoryId = "categoryId_example"; // String | 达人分类ID
String pageNum = "pageNum_example"; // String | 页码
String pageSize = "pageSize_example"; // String | 每页显示几条记录
try {
    ForumTalentsListModel result = apiInstance.v10ForumTalentsListGet(categoryId, pageNum, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10ForumTalentsListGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **categoryId** | **String**| 达人分类ID |
 **pageNum** | **String**| 页码 | [optional]
 **pageSize** | **String**| 每页显示几条记录 | [optional]

### Return type

[**ForumTalentsListModel**](ForumTalentsListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10ForumTalkTopicsListGet"></a>
# **v10ForumTalkTopicsListGet**
> TalkTopicsListModel v10ForumTalkTopicsListGet(pageNum, pageSize)



版块内搜索帖子 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String pageNum = "pageNum_example"; // String | 页码
String pageSize = "pageSize_example"; // String | 每页记录数
try {
    TalkTopicsListModel result = apiInstance.v10ForumTalkTopicsListGet(pageNum, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10ForumTalkTopicsListGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **pageNum** | **String**| 页码 | [optional]
 **pageSize** | **String**| 每页记录数 | [optional]

### Return type

[**TalkTopicsListModel**](TalkTopicsListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10ForumTopicPost"></a>
# **v10ForumTopicPost**
> OptSuccessModel v10ForumTopicPost(boardId, topicTypeId, title, content, pics)



发帖 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String boardId = "boardId_example"; // String | 版块ID
String topicTypeId = "topicTypeId_example"; // String | 帖子类别ID
String title = "title_example"; // String | 标题
String content = "content_example"; // String | 内容
List<String> pics = Arrays.asList("pics_example"); // List<String> | 帖子图片地址
try {
    OptSuccessModel result = apiInstance.v10ForumTopicPost(boardId, topicTypeId, title, content, pics);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10ForumTopicPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **boardId** | **String**| 版块ID |
 **topicTypeId** | **String**| 帖子类别ID |
 **title** | **String**| 标题 |
 **content** | **String**| 内容 |
 **pics** | [**List&lt;String&gt;**](String.md)| 帖子图片地址 |

### Return type

[**OptSuccessModel**](OptSuccessModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10ForumTopicTopicIdGet"></a>
# **v10ForumTopicTopicIdGet**
> ForumTopicModel v10ForumTopicTopicIdGet(topicId, pageNum, pageSize)



获取帖子详情 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String topicId = "topicId_example"; // String | 用户ID
String pageNum = "pageNum_example"; // String | 回复列表页码
String pageSize = "pageSize_example"; // String | 回复列表每页记录数
try {
    ForumTopicModel result = apiInstance.v10ForumTopicTopicIdGet(topicId, pageNum, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10ForumTopicTopicIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **topicId** | **String**| 用户ID |
 **pageNum** | **String**| 回复列表页码 | [optional]
 **pageSize** | **String**| 回复列表每页记录数 | [optional]

### Return type

[**ForumTopicModel**](ForumTopicModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10ForumTopicTopicIdReplyPost"></a>
# **v10ForumTopicTopicIdReplyPost**
> OptSuccessModel v10ForumTopicTopicIdReplyPost(topicId, sourcePid, content, pics)



回帖 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String topicId = "topicId_example"; // String | 帖子ID
String sourcePid = "sourcePid_example"; // String | 回复目标帖子的ID
String content = "content_example"; // String | 回帖内容
List<String> pics = Arrays.asList("pics_example"); // List<String> | 帖子图片地址
try {
    OptSuccessModel result = apiInstance.v10ForumTopicTopicIdReplyPost(topicId, sourcePid, content, pics);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10ForumTopicTopicIdReplyPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **topicId** | **String**| 帖子ID |
 **sourcePid** | **String**| 回复目标帖子的ID |
 **content** | **String**| 回帖内容 |
 **pics** | [**List&lt;String&gt;**](String.md)| 帖子图片地址 |

### Return type

[**OptSuccessModel**](OptSuccessModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10ForumTopicsListGet"></a>
# **v10ForumTopicsListGet**
> ForumTopicsListModel v10ForumTopicsListGet(type, pageNum, pageSize)



获取帖子列表 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String type = "type_example"; // String | 类型 - 1：晒单帖 2：热帖 3：达人发布的帖子
String pageNum = "pageNum_example"; // String | 页码
String pageSize = "pageSize_example"; // String | 每页记录数
try {
    ForumTopicsListModel result = apiInstance.v10ForumTopicsListGet(type, pageNum, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10ForumTopicsListGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **type** | **String**| 类型 - 1：晒单帖 2：热帖 3：达人发布的帖子 |
 **pageNum** | **String**| 页码 | [optional]
 **pageSize** | **String**| 每页记录数 | [optional]

### Return type

[**ForumTopicsListModel**](ForumTopicsListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10ForumTransshipperTransshipperIdForumTopicsListGet"></a>
# **v10ForumTransshipperTransshipperIdForumTopicsListGet**
> TransshipperForumTopicsListModel v10ForumTransshipperTransshipperIdForumTopicsListGet(transshipperId)



转运公司详情页帖子列表 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String transshipperId = "transshipperId_example"; // String | 转运公司ID
try {
    TransshipperForumTopicsListModel result = apiInstance.v10ForumTransshipperTransshipperIdForumTopicsListGet(transshipperId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10ForumTransshipperTransshipperIdForumTopicsListGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **transshipperId** | **String**| 转运公司ID |

### Return type

[**TransshipperForumTopicsListModel**](TransshipperForumTopicsListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10SearchingKeywordsBoardsGet"></a>
# **v10SearchingKeywordsBoardsGet**
> SearchingBoardsModel v10SearchingKeywordsBoardsGet(keywords)



获取版块列表 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String keywords = "keywords_example"; // String | 关键词
try {
    SearchingBoardsModel result = apiInstance.v10SearchingKeywordsBoardsGet(keywords);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10SearchingKeywordsBoardsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **keywords** | **String**| 关键词 |

### Return type

[**SearchingBoardsModel**](SearchingBoardsModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10SearchingKeywordsForumBoardBoardIdTopicsListGet"></a>
# **v10SearchingKeywordsForumBoardBoardIdTopicsListGet**
> SearchingForumBoardTopicsListModel v10SearchingKeywordsForumBoardBoardIdTopicsListGet(keywords, boardId, topicTypeId, topicOrder, pageNum, pageSize)



版块内搜索帖子 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String keywords = "keywords_example"; // String | 关键词
String boardId = "boardId_example"; // String | 版块ID
String topicTypeId = "topicTypeId_example"; // String | 主题类型ID
String topicOrder = "topicOrder_example"; // String | 主题排序方式 - 格式：field order 允许的字段名lastpost dateline，例如dateline desc, lastpost asc
String pageNum = "pageNum_example"; // String | 版块主题页码
String pageSize = "pageSize_example"; // String | 版块主题每页记录数
try {
    SearchingForumBoardTopicsListModel result = apiInstance.v10SearchingKeywordsForumBoardBoardIdTopicsListGet(keywords, boardId, topicTypeId, topicOrder, pageNum, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10SearchingKeywordsForumBoardBoardIdTopicsListGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **keywords** | **String**| 关键词 |
 **boardId** | **String**| 版块ID |
 **topicTypeId** | **String**| 主题类型ID | [optional]
 **topicOrder** | **String**| 主题排序方式 - 格式：field order 允许的字段名lastpost dateline，例如dateline desc, lastpost asc | [optional]
 **pageNum** | **String**| 版块主题页码 | [optional]
 **pageSize** | **String**| 版块主题每页记录数 | [optional]

### Return type

[**SearchingForumBoardTopicsListModel**](SearchingForumBoardTopicsListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10SearchingKeywordsTagsListGet"></a>
# **v10SearchingKeywordsTagsListGet**
> SearchingTagsListModel v10SearchingKeywordsTagsListGet(keywords, pageNum, pageSize)



搜索标签 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String keywords = "keywords_example"; // String | 关键词
String pageNum = "pageNum_example"; // String | 页码
String pageSize = "pageSize_example"; // String | 每页记录数
try {
    SearchingTagsListModel result = apiInstance.v10SearchingKeywordsTagsListGet(keywords, pageNum, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10SearchingKeywordsTagsListGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **keywords** | **String**| 关键词 |
 **pageNum** | **String**| 页码 | [optional]
 **pageSize** | **String**| 每页记录数 | [optional]

### Return type

[**SearchingTagsListModel**](SearchingTagsListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10StoreStoreIdForumTopicsListGet"></a>
# **v10StoreStoreIdForumTopicsListGet**
> StoreForumTopicsListModel v10StoreStoreIdForumTopicsListGet(storeId, pageNum, pageSize)



商家详情帖子列表 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String storeId = "storeId_example"; // String | 商家ID
String pageNum = "pageNum_example"; // String | 页码
String pageSize = "pageSize_example"; // String | 每页记录数
try {
    StoreForumTopicsListModel result = apiInstance.v10StoreStoreIdForumTopicsListGet(storeId, pageNum, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10StoreStoreIdForumTopicsListGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **storeId** | **String**| 商家ID |
 **pageNum** | **String**| 页码 | [optional]
 **pageSize** | **String**| 每页记录数 | [optional]

### Return type

[**StoreForumTopicsListModel**](StoreForumTopicsListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10TagTagIdDealsListGet"></a>
# **v10TagTagIdDealsListGet**
> TagDealsListModel v10TagTagIdDealsListGet(tagId, pageNum, pageSize)



获取标签关联的优惠列表 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String tagId = "tagId_example"; // String | 标签ID
String pageNum = "pageNum_example"; // String | 页码
String pageSize = "pageSize_example"; // String | 每页记录数
try {
    TagDealsListModel result = apiInstance.v10TagTagIdDealsListGet(tagId, pageNum, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10TagTagIdDealsListGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **tagId** | **String**| 标签ID |
 **pageNum** | **String**| 页码 | [optional]
 **pageSize** | **String**| 每页记录数 | [optional]

### Return type

[**TagDealsListModel**](TagDealsListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10TagTagIdTopicsListGet"></a>
# **v10TagTagIdTopicsListGet**
> TagTopicsListModel v10TagTagIdTopicsListGet(tagId, type, pageNum, pageSize)



获取和标签关联的帖子列表 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String tagId = "tagId_example"; // String | 标签ID
String type = "type_example"; // String | 类型 - 1：普通帖子 2：晒单
String pageNum = "pageNum_example"; // String | 页码
String pageSize = "pageSize_example"; // String | 每页记录数
try {
    TagTopicsListModel result = apiInstance.v10TagTagIdTopicsListGet(tagId, type, pageNum, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10TagTagIdTopicsListGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **tagId** | **String**| 标签ID |
 **type** | **String**| 类型 - 1：普通帖子 2：晒单 |
 **pageNum** | **String**| 页码 | [optional]
 **pageSize** | **String**| 每页记录数 | [optional]

### Return type

[**TagTopicsListModel**](TagTopicsListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10TagsListGet"></a>
# **v10TagsListGet**
> TagsListModel v10TagsListGet(pageNum, pageSize)



获取热门标签列表 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String pageNum = "pageNum_example"; // String | 页码
String pageSize = "pageSize_example"; // String | 每页记录数
try {
    TagsListModel result = apiInstance.v10TagsListGet(pageNum, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10TagsListGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **pageNum** | **String**| 页码 | [optional]
 **pageSize** | **String**| 每页记录数 | [optional]

### Return type

[**TagsListModel**](TagsListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10UserCollectionDelete"></a>
# **v10UserCollectionDelete**
> OptSuccessModel v10UserCollectionDelete(type, dataId)



用户取消收藏动作 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String type = "type_example"; // String | 收藏类型 - 1：收藏优惠 2：收藏转运 3：收藏商家 4：收藏晒单 5：收藏论坛版块 6：收藏帖子
String dataId = "dataId_example"; // String | 收藏的数据的ID
try {
    OptSuccessModel result = apiInstance.v10UserCollectionDelete(type, dataId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10UserCollectionDelete");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **type** | **String**| 收藏类型 - 1：收藏优惠 2：收藏转运 3：收藏商家 4：收藏晒单 5：收藏论坛版块 6：收藏帖子 |
 **dataId** | **String**| 收藏的数据的ID |

### Return type

[**OptSuccessModel**](OptSuccessModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10UserCollectionForumBoardsListGet"></a>
# **v10UserCollectionForumBoardsListGet**
> UserCollectionForumBoardsListModel v10UserCollectionForumBoardsListGet(pageNum, pageSize)



用户收藏的论坛版块 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String pageNum = "pageNum_example"; // String | 页码
String pageSize = "pageSize_example"; // String | 每页记录数
try {
    UserCollectionForumBoardsListModel result = apiInstance.v10UserCollectionForumBoardsListGet(pageNum, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10UserCollectionForumBoardsListGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **pageNum** | **String**| 页码 | [optional]
 **pageSize** | **String**| 每页记录数 | [optional]

### Return type

[**UserCollectionForumBoardsListModel**](UserCollectionForumBoardsListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10UserCollectionForumTopicsListGet"></a>
# **v10UserCollectionForumTopicsListGet**
> UserCollectionForumTopicsListModel v10UserCollectionForumTopicsListGet(pageNum, pageSize)



用户收藏的帖子 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String pageNum = "pageNum_example"; // String | 页码
String pageSize = "pageSize_example"; // String | 每页记录数
try {
    UserCollectionForumTopicsListModel result = apiInstance.v10UserCollectionForumTopicsListGet(pageNum, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10UserCollectionForumTopicsListGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **pageNum** | **String**| 页码 | [optional]
 **pageSize** | **String**| 每页记录数 | [optional]

### Return type

[**UserCollectionForumTopicsListModel**](UserCollectionForumTopicsListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10UserCollectionPost"></a>
# **v10UserCollectionPost**
> OptSuccessModel v10UserCollectionPost(type, dataId)



用户收藏动作 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String type = "type_example"; // String | 收藏类型 - 1：收藏优惠 2：收藏转运 3：收藏商家 4：收藏晒单 5：收藏论坛版块 6：收藏帖子
String dataId = "dataId_example"; // String | 收藏的数据的ID
try {
    OptSuccessModel result = apiInstance.v10UserCollectionPost(type, dataId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10UserCollectionPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **type** | **String**| 收藏类型 - 1：收藏优惠 2：收藏转运 3：收藏商家 4：收藏晒单 5：收藏论坛版块 6：收藏帖子 |
 **dataId** | **String**| 收藏的数据的ID |

### Return type

[**OptSuccessModel**](OptSuccessModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10UserFriendsForumTopicsListGet"></a>
# **v10UserFriendsForumTopicsListGet**
> FriendsForumTopicsListModel v10UserFriendsForumTopicsListGet(pageNum, pageSize)



好友发布的帖子列表 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String pageNum = "pageNum_example"; // String | 页码
String pageSize = "pageSize_example"; // String | 每页记录数
try {
    FriendsForumTopicsListModel result = apiInstance.v10UserFriendsForumTopicsListGet(pageNum, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10UserFriendsForumTopicsListGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **pageNum** | **String**| 页码 | [optional]
 **pageSize** | **String**| 每页记录数 | [optional]

### Return type

[**FriendsForumTopicsListModel**](FriendsForumTopicsListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10UserPraisingDelete"></a>
# **v10UserPraisingDelete**
> OptSuccessModel v10UserPraisingDelete(type, dataId)



用户取消点赞 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String type = "type_example"; // String | 取消赞类型 - 1：优惠评论取消赞 2：商家评论取消赞 3：转运评论取消赞 4：帖子评论取消赞 5：帖子取消赞
String dataId = "dataId_example"; // String | 取消赞的数据的ID
try {
    OptSuccessModel result = apiInstance.v10UserPraisingDelete(type, dataId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10UserPraisingDelete");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **type** | **String**| 取消赞类型 - 1：优惠评论取消赞 2：商家评论取消赞 3：转运评论取消赞 4：帖子评论取消赞 5：帖子取消赞 |
 **dataId** | **String**| 取消赞的数据的ID |

### Return type

[**OptSuccessModel**](OptSuccessModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10UserPraisingPost"></a>
# **v10UserPraisingPost**
> OptSuccessModel v10UserPraisingPost(dataId, type)



用户点赞动作 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String dataId = "dataId_example"; // String | 点赞的数据的ID
String type = "type_example"; // String | 点赞类型 - 1：优惠评论点赞 2：商家评论点赞 3：转运评论点赞 4：帖子评论点赞 5：帖子点赞
try {
    OptSuccessModel result = apiInstance.v10UserPraisingPost(dataId, type);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10UserPraisingPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dataId** | **String**| 点赞的数据的ID |
 **type** | **String**| 点赞类型 - 1：优惠评论点赞 2：商家评论点赞 3：转运评论点赞 4：帖子评论点赞 5：帖子点赞 | [optional]

### Return type

[**OptSuccessModel**](OptSuccessModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="v10UserUidForumTopicsListGet"></a>
# **v10UserUidForumTopicsListGet**
> UserForumTopicsListModel v10UserUidForumTopicsListGet(uid, pageNum, pageSize)



获取指定用户发布的帖子列表 

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String uid = "uid_example"; // String | 用户ID
String pageNum = "pageNum_example"; // String | 页码
String pageSize = "pageSize_example"; // String | 每页记录数
try {
    UserForumTopicsListModel result = apiInstance.v10UserUidForumTopicsListGet(uid, pageNum, pageSize);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#v10UserUidForumTopicsListGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **uid** | **String**| 用户ID |
 **pageNum** | **String**| 页码 | [optional]
 **pageSize** | **String**| 每页记录数 | [optional]

### Return type

[**UserForumTopicsListModel**](UserForumTopicsListModel.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

