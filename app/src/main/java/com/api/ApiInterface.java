package com.api;

import com.Model.Client_collection_model;
import com.Model.Document_categary_model;
import com.Model.Gen_Response_Model;
import com.Model.GenResModel;
import com.Model.NotificationResponseModel;
import com.Model.PetActivityListResponse;
import com.Model.PetDetailsModel;

import com.Model.Pet_Activity_Model;
import com.Model.Pet_Search_Model;
import com.Model.PostResponseModel;
import com.Model.Res_FoundQues_Model;
import com.Model.ResponseModel;
import com.Model.SearchPersonResponseShare;
import com.Model.SharedListModel;
import com.Model.UserModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface ApiInterface {

    @POST("registration_parent.php")
    Observable<GenResModel> _reg_user(@Query("name") String usrName, @Query("mobile") String mpbileNo, @Query("email") String email);


    @POST("login_auth_parent.php")
    Observable<GenResModel> _lgn_user(@Query("name") String usrName, @Query("mobile") String mpbileNo);


    @POST("registration_auth_parent.php")
    Observable<UserModel> _otp_verify(@Query("otp") String otpCode, @Query("mobile") String mpbileNo,@Query("type") String type_val);


    @POST("pet_list.php")
    Observable<Client_collection_model> _getClients(@Query("owner_id") String OwnerId, @Query("mobile") String mobile);

    @POST("pet_creation.php")
    @FormUrlEncoded
    Observable<Gen_Response_Model> _createClient(@Query("owner_id") String OwnerId, @Query("pet_name") String Pet_Name,
                                                 @Query("dob") String DOB, @Query("brand") String Brand, @Query("protein") String Protein,
                                                 @Query("portion_size") String Portion_Size,@Field("image") String petImage);


    @POST("pet_owner_update.php")
    @FormUrlEncoded
    Observable<Gen_Response_Model> _Parnt_Prof_Update(@Query("owner_id") String OwnerId, @Query("name") String Name,
                                                      @Query("mobile") String Mobile, @Query("preferred_contact") String pref_cont,
                                                      @Query("address") String address, @Field("image") String prof_image,@Query("email") String Email);


    @POST("pet_post_creation.php")
    @FormUrlEncoded
    Observable<Gen_Response_Model> _pet_post_creation(@Query("owner_id") String OwnerId, @Query("pet_id") String PetId, @Query("description") String description, @Field("image") String imagePost);


    @POST("pet_list_by_follower.php")
    Observable<Pet_Search_Model> _getClientsBySearch(@Query("owner_id") String OwnerId,@Query("pet_id") String PetId, @Query("mobile") String mobile);

    @POST("pet_posts_details.php")
    Observable<PostResponseModel> _getPetFeedDtls(@Query("pet_id") String OwnerId, @Query("owner_id") String mobile, @Query("limit") String pag_index);


    @POST("pet_follow.php")
    Observable<Gen_Response_Model>  _toFollow(@Query("pet_id") String pet_id, @Query("follower_id") String follower_ID);

    @POST("pet_post_edit.php")
    @FormUrlEncoded
    Observable<Gen_Response_Model> _edit_Post(@Query("pet_id") String pet_id, @Query("owner_id") String owner_Id, @Query("posts_id") String PostId, @Query("description") String Desc, @Field("image") String Image);



    @POST("delete_pet_posts.php")
    Observable<ResponseModel> _del_Post(@Query("pet_id") String pet_id, @Query("owner_id") String owner_Id, @Query("pet_posts_id") String PostId);


    @POST("pet_posts_likes.php")
    Observable<Gen_Response_Model> _like_Post(@Query("pet_id") String pet_id, @Query("pet_posts_id") String PostId);

    @POST("pet_posts_comments.php")
    Observable<Gen_Response_Model> _post_comments(@Query("pet_id") String pet_id, @Query("pet_posts_id") String PostId, @Query("comment") String _comment);

    @POST("pet_posts_comments_list.php")
    Observable<Pet_Search_Model> _comments_list(@Query("pet_posts_id") String PostId);


    @POST("share_pet_posts.php")
    Observable<Gen_Response_Model> _share_post(@Query("pet_id") String pet_id, @Query("owner_id") String owner_Id, @Query("pet_posts_id") String PostId);

    @POST("pet_unfollow.php")
    Observable<ResponseModel> _unfollow_pet(@Query("pet_id") String pet_id, @Query("follower_id") String followerId);

    @POST("master_data.php")
    Observable<List<Pet_Activity_Model>> _pet_act_list(@Query("master_data") String mas_data);

    @POST("pet_activity_creation.php")
    @FormUrlEncoded
    Observable<Gen_Response_Model> _cr_pet_activity(@Query("owner_id") String owner_Id, @Query("pet_id") String Pet_ID, @Query("activity_list_id") String activity_list_Id, @Query("details") String Opt_details, @Field("audio_file") String audio_File);

    @POST("pet_activity_list.php")
    Observable<PetActivityListResponse> _pet_activity_list(@Query("owner_id") String Owner_Id, @Query("pet_id") String pet_Id);

    @POST("pet_details.php")
    Observable<PetDetailsModel> _pet_details(@Query("pet_id") String pet_Id);


    @POST("pet_notes_creation.php")
    Observable<Gen_Response_Model> _notes_Creation(@Query("user_id") String Owner_Id,@Query("pet_id") String pet_id,@Query("notes") String Notes,@Query("user_type") String usrType);

    @POST("pet_food_preferences_update.php")
    Observable<Gen_Response_Model> _food_update(@Query("owner_id") String Owner_Id,@Query("pet_id") String pet_id,@Query("brand") String brand,@Query("protein") String Protein,@Query("portion_size") String servingVal);

    @POST("master_data.php")
    Observable<List<Document_categary_model>> _doc_categ(@Query("master_data") String mas_data);

    @POST("temp_pet_documents_creation.php")
    @FormUrlEncoded
    Observable<Gen_Response_Model> _create_record(@Query("user_id") String UserId,@Query("pet_id") String PetId,@Query("name") String Name,@Query("document_category_id") String sel_catg_id,@Field("image") String Image,@Query("user_type") String UserType);

    @POST("business_parent_search.php")
    Observable<SearchPersonResponseShare> _search_parent_business(@Query("owner_name") String owner,@Query("pet_name") String petname,@Query("name") String Name,@Query("owner_id") String Owner_id,@Query("pet_id") String PetId,@Query("mobile") String mobile);



    @POST("pet_profile_share_list.php")
    Observable<SharedListModel> _shared_list(@Query("pet_id") String pet_id, @Query("owner_id") String owner_id);

    @POST( "pet_profile_share.php")
    Observable<Gen_Response_Model> _to_share_profile(@Query("pet_id") String pet_id, @Query("owner_id") String owner_id,@Query("owner_name") String ownerName,@Query("pet_name") String PetName,@Query("user_id") String UserId,@Query("user_type") String user_type,@Query("user_mobile") String user_mobile);

    @POST("delete_pet_share_list.php")
    Observable<ResponseModel> _del_shared_contact(@Query("owner_id") String ownerId, @Query("share_id") String ShareId,@Query("pet_type") String PetType);

    @POST("owner_pet_notification.php")
    Observable<NotificationResponseModel> _nfy_list(@Query("owner_id") String owner_id);

    @POST("vetx_questions_list.php")
    Observable<Res_FoundQues_Model> _find_questions(@Query("owner_id") String owner_id, @Query("search_key") String SrchKey, @Query("keyword") String filter, @Query("limit") String limitVal);

    /*@GET
    Call<ResponseBody> fetchImage(@Url String url);*/


    @POST("pet_photo_edit.php")
    @FormUrlEncoded
    Observable<Gen_Response_Model> _pet_photo_update(@Query("pet_id") String PetId,@Field("image") String Image);

    @POST("posts_reporting.php")
    Observable<ResponseModel> _report_post(@Query("pet_id ") String PetId,@Query("reporting_owner_id ") String ownerId,@Query("post_id") String PostId,@Query("comment") String Comment);

    @POST("pet_parent_blocking.php")
    Observable<Gen_Response_Model> _block_user(@Query("owner_id") String OwnerId,@Query("blocked_by") String _block_by);

    @POST("accept_or_deny.php")
    Observable<Gen_Response_Model> _allow_or_deny(@Query("type") String type,@Query("reaction") String reaction,@Query("reaction_id") String reaction_id);


}
