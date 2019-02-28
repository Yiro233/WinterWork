function writeWB() {//调用函数
    loadInfo();
    layerDisplay(1)
}
window.onload = loadInfo();
function loadInfo(){//加载个人信息
    $.ajax({
        type: "GET",
        url: "UserInfoServlet",
        enctype: "multipart/form-data",
        success: function (data) {
            var WB_data = JSON.parse(data);
            var nickname = document.getElementById("nickname");
            var headpic = document.getElementById("headpic");
            var follow = document.getElementById("follow");
            var followed = document.getElementById("followed");
            var blog_num = document.getElementById("blog_num");
            var self_intro = document.getElementById("self_intro");
            var collect_num = document.getElementById("collect_num");
            var headinComment=document.getElementById("headpic").src;
            nickname.innerHTML = WB_data.nickname;
            self_intro.innerHTML = WB_data.self_introduction
            headpic.src = WB_data.avatar;
            headpic.alt = WB_data.nickname;
            follow.innerHTML = WB_data.follow;
            followed.innerHTML = WB_data.followed;
            blog_num.innerHTML = WB_data.blog_num;
            collect_num.innerHTML = WB_data.collect_num;
            $.ajax({
                type: "GET",
                url: "PersonalMessageServlet",
                enctype: "multipart/form-data",
                success: function (result) {
                    var WB_data = JSON.parse(result).data;
                    $(`#WB_display`).html(`<div class="WB_cardwrap WB_empty_height S_bg2" id="WB_empty" style="display: ${WB_data.length == 0 ? "block":"none"};">
                                                <div class="WB_empty">
                                                    <div class="WB_innerwrap">
                                                        <div class="empty_con clearfix">
                                                            <p class="icon_bed"><i class="W_icon icon_warnB"></i></p>
                                                            <p class="text">
                                                                <a href="javascript:void(0);" onclick="layerDisplay(1)">你还没有发过微博，点这里发一条微博。</a>
                                                            </p>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>`)
                    if (WB_data.length) {
                        for (var i = 0; i < WB_data.length; i++) {
                            $('#WB_display').append(`<div class="blob_box_info" id=${WB_data[i].id} style="display: block;">
                                <div class="inner_detail">
                                    <div class="perInfo">
                                        <div class="headpic_box">
                                            <img src=${WB_data[i].avatar} alt="" id="headpic_inBox_${WB_data[i].id}" class="headpic_inBox" width="50px;" height="50px;">
                                        </div>
                                        <div class="moreInfo">
                                            <div id="nameInBox_${WB_data[i].id}" class="nameInBox">${WB_data[i].user_nickname}</div>
                                            <div id="timeInBox_${WB_data[i].id}" class="timeInBox">${WB_data[i].date}</div>
                                        </div>
                                    </div>
                                    <div class="blobCont">
                                        <div class="textCont" id="textCont_${WB_data[i].id}">${WB_data[i].content}</div>
                                        <div class="picCont_box" style="display:${WB_data[i].pic ? "block" : "none"}">
                                            <a><img src="${WB_data[i].pic}" title="${WB_data[i].pic ? "微博图片-点击查看大图" : ""}" id="picCont_${WB_data[i].id}" class="picCont" width="50%" onclick="pictureView(${WB_data[i].id})"></a>
                                        </div>
                                        <div class="video_box" style="display:${WB_data[i].video ? "block" : "none"}">
                                            <video src="${WB_data[i].video}" controls="controls" width="50%"></video>                                
                                        </div>
                                    </div>
                                </div>
                                <div class="WB_handle">
                                    <ul class="WB_row_line WB_row_r4 clearfix S_line2">
                                        <li> 
                                            <a class="S_txt2"><span class="pos"><span class="line S_line1"><span><em class="W_ficon ficon_favorite S_ficon" id="collect_${WB_data[i].id}" onclick="collect(${WB_data[i].id},${WB_data[i].id})">û</em><em id="collectText_${WB_data[i].id}" class="collectText">收藏</em></span></span></span></a>
                                        </li>
                                        <li>
                                            <a class="S_txt2"><span class="pos"><span class="line S_line1"><span><em class="W_ficon ficon_forward S_ficon"></em><em></em></span></span></span></a>
                                        </li>
                                        <li class="">
                                            <a class="S_txt2" title="评论"><span class="pos"><span class="line S_line1"><span><em class="W_ficon ficon_repeat S_ficon" id="comment_${WB_data[i].id}" onclick="commentDisplay(${WB_data[i].id},${WB_data[i].id})"></em><em id="commentNum_${WB_data[i].id}" class="commuentNum">${WB_data[i].content_num != 0 ? WB_data[i].content_num : "评论"}</em></span></span></span></a>
                                        </li>
                                        <li class="">
                                            <a class="S_txt2" title="赞"><span class="pos"><span class="line S_line1"><span class=""><em class="W_ficon ficon_praised S_txt2" id="thumb_${WB_data[i].id}" onclick="thumb(${WB_data[i].id},${WB_data[i].id})">ñ</em><em id="likeNum_${WB_data[i].id}" class="likeNum">${WB_data[i].like != 0 ? WB_data[i].like : "赞"}</em></span></span></span></a>
                                        </li>
                                    </ul>
                                </div>
                            </div>`);
                            $(`#${WB_data[i].id}`).append(`<div class="S_bg2 clearfix send_weibo_long commentBox"id="commentBox_${WB_data[i].id}" node-type="wrap" style="display:none;">
                                <div class="sendCommentBox">
                                    <div class="commentPic">
                                    <img src="${headinComment}" alt="" height="50px" width="50px;" style="border-radius: 50%;">
                                </div>
                                <div class="input_option" style="display:inline-block; width:70%;">
                                    <div class="input" node-type="textElDiv" style="height: 20px; min-height: 20px;">
                                        <textarea class="W_input" id="sendComment_${WB_data[i].id}" title="微博输入框" name="content" node-type="textEl" pic_split="1" range="0&0" style="overflow: hidden; height: 20px; min-height: 20px; width: 100%;" ></textarea>
                                        <div class="send_succpic send_succpicCom" id="sendSuc_${WB_data[i].id}" style="display:none;" node-type="successTip"><span class="W_icon icon_succB"></span><span
                                            class="txt">发布评论成功</span>
                                        </div>
                                    </div>
                                    <div class="kind kind_comment">
                                        <a class="S_txt1" href="javascript:void(0);" action-type="multiimage" action-data="type=508&action=1&log=image&cate=1"
                                            title="图片" suda-uatrack="key=tblog_new_image_upload&value=image_button" style="position: relative;" onclick="fileFunc('image_file_${WB_data[i].id}',${WB_data[i].id})">
                                            <em class="W_ficon ficon_image">p</em>图片
                                            <div style="position: absolute; left: 0px; top: 0px; display: block; overflow: hidden; background-color: rgb(0, 0, 0); opacity: 0; width: 49px; height: 24px;">
                                                <input type="file" accept="image/jpg,image/jpeg,image/png,image/gif" hidefoucs="true" node-type="fileBtn"
                                                    name="pic1" style="cursor:pointer;width:1000px;height:1000px;position:absolute;bottom:0;right:0;font-size:200px;"
                                                    id="image_file_${WB_data[i].id}" multiple="multiple">
                                            </div>
                                        </a>
                                        <a class="S_txt1" href="javascript:void(0);" action-type="video" action-data="type=502&action=1&log=video&cate=1"
                                            title="视频" suda-uatrack="key=tblog_home_edit&value=video_button" style="position: relative;" onclick="fileFunc('video_file_${WB_data[i].id}',${WB_data[i].id})">
                                            <em class="W_ficon ficon_video">q</em>视频<div style="position: absolute; left: 0px; top: 0px; display: block; overflow: hidden; background-color: rgb(0, 0, 0); opacity: 0; width: 49px; height: 24px;">
                                                <input type="file" name="video" hidefoucs="true" title="视频" accept=".mpg,.m4v,.mp4,.flv,.3gp,.mov,.avi,.rmvb,.mkv,.wmv"
                                                    style="cursor:pointer;width:1000px;height:1000px;position:absolute;bottom:0;right:0;font-size:200px;"
                                                    id="video_file_${WB_data[i].id}">
                                            </div>
                                        </a>
                                        <div class="file_info file_info_com" id="file_info_${WB_data[i].id}" style="display:inline-block"></div>
                                    </div>
                                </div>
                                <div class="sendCommentBtn">
                                        <a href="javascript:void(0);" class="W_btn_a btn_30px W_btn_a_disable" diss-data="module=stissue" type="submit"
                                            onclick="sendComment(${WB_data[i].id})" title="发布评论按钮" id="publishBtn">发布评论</a>
                                </div>
                                </div>
                                <div class="commentViewBox" id="commentViewBox_${WB_data[i].id}">
                                </div>
                            </div>`);
                            $.ajax({
                                type: "POST",
                                url: "LikeServlet",
                                enctype: "multipart/form-data",
                                data: { "loadid": WB_data[i].id },
                                async: false,
                                success: function (result) {
                                    var thumb = document.getElementById(`thumb_${WB_data[i].id}`);
                                    if (result == "like") {
                                        thumb.style.color = "#ee7700";
                                        thumb.title = "取消赞";
                                    }
                                    else if (result == "cancel") {
                                        thumb.style.color = "#808080";
                                        thumb.title = "赞";
                                    }
                                }
                            })
                            $.ajax({
                                type: "POST",
                                url: "CollectServlet",
                                enctype: "multipart/form-data",
                                data: { "load_collect_id": WB_data[i].id },
                                async: false,
                                success: function (result) {
                                    var collected = document.getElementById(`collect_${WB_data[i].id}`);;
                                    var collectText = document.getElementById(`collectText_${WB_data[i].id}`);;
                                    if (result == "collect") {
                                        collected.style.color = "#ee7700";
                                        collected.title = "取消收藏";
                                        collectText.innerHTML = "已收藏";
                                    }
                                    else if (result == "cancel") {
                                        collected.style.color = "#808080";
                                        collected.title = "收藏";
                                        collectText.innerHTML = "收藏"
                                    }
                                }
                            })
                        }
                        for (var i = 0; i < WB_data.length; i++) {
                            $(`#${WB_data[i].id}`).animate({ right: `0px`, opacity: "1" }, 500); 
                        }                      
                    }
                }
            })
        }
    })
}
function showFollow() {//显示关注
    $.ajax({
        type: "GET",
        url: "FollowInfoServlet",
        enctype: "multipart/form-data",
        async: false,
        success: function (result) {
            var item = JSON.parse(result).data;
            $(`#WB_display`).html(``);
            for (var i = 0;i < item.length;i++) {
                $(`#WB_display`).append(`<div class="blob_box_info" id="${item[i].id}">
                    <div class="inner_detail" style="padding:20px;">
                        <div class="perInfo">
                            <div class="headpic_box">
                                <img src="${item[i].avatar}" alt="${item[i].nickname}" id="sendWBpic" class="headpic_inBox" width="50px;" height="50px;">
                            </div>
                            <div class="moreInfo">
                                <div class="nameInBox">
                                    <span id="nameInBox_${item[i].id}">${item[i].nickname}</span>
                                    <button class="followBtn_info followBtn_${item[i].nickname}" id="followBtn_${item[i].id}" title="取消关注" style="display:block" onclick="follow(${item[i].id})">已关注</button>
                                </div>
                                <div id="self_intro_${item[i].id}" class="timeInBox">${item[i].self_introduction}</div>
                            </div>
                        </div>
                    </div>
                </div>`)
                $(`#${item[i].id}`).animate({ right: `0px`, opacity: "1" }, 500);
            }
        }
    })
}
function loadCollect() {//加载收藏
    $.ajax({
        type: "GET",
        url: "CollectInfoServlet",
        enctype: "multipart/form-data",
        success: function (data) {
            $(`#WB_display`).html(``);
            var dataObj = JSON.parse(data);
            var item = dataObj.data;
            var nick = document.getElementById("nickname").innerText;
            var headinComment = document.getElementById("headpic").src;
            for (var i = 0; i < item.length; i++) {
                $('#WB_display').append(`<div class="blob_box_info" id="${item[i].id}" style="display: block;">
                                <div class="inner_detail">
                                    <div class="perInfo">
                                        <div class="headpic_box">
                                            <img src="${item[i].avatar}" alt="" id="headpic_inBox_${item[i].id}" class="headpic_inBox" width="50px;" height="50px;">
                                        </div>
                                        <div class="moreInfo">
                                            <div class="nameInBox">
                                                <span id="nameInBox_${item[i].id}">${item[i].user_nickname}</span>
                                                <button class="followBtn followBtn_${item[i].user_nickname}" id="followBtn_${item[i].id}" title="关注" style="display: ${nick == item[i].user_nickname ? "none" : "block"}" onclick="follow(${item[i].id})">关注</button>
                                            </div>
                                            <div id="timeInBox_${item[i].id}" class="timeInBox">${item[i].date}</div>
                                        </div>
                                    </div>
                                    <div class="blobCont">
                                        <div class="textCont" id="textCont_${item[i].id}">${item[i].content}</div>
                                        <div class="picCont_box" style="display:${item[i].pic ? "block" : "none"}">
                                            <a><img src="${item[i].pic}" title="${item[i].pic ? "微博图片-点击查看大图" : ""}" id="picCont_${item[i].id}" class="picCont" width="50%" onclick="pictureView(${item[i].id})"></a>
                                        </div>
                                        <div class="video_box" style="display:${item[i].video ? "block" : "none"}">
                                            <video src="${item[i].video}" controls="controls" width="50%"></video>                                
                                        </div>
                                    </div>
                                </div>
                                <div class="WB_handle">
                                    <ul class="WB_row_line WB_row_r4 clearfix S_line2">
                                        <li>
                                            <a class="S_txt2"><span class="pos"><span class="line S_line1"><span><em class="W_ficon ficon_favorite S_ficon" id="collect_${item[i].id}" style="color: #ee7700;" onclick="collect(${item[i].id},${item[i].id})">û</em><em id="collectText_${item[i].id}" class="collectText">已收藏</em></span></span></span></a>
                                        </li>
                                        <li>
                                            <a class="S_txt2"><span class="pos"><span class="line S_line1"><span><em class="W_ficon ficon_forward S_ficon"></em><em></em></span></span></span></a>
                                        </li>
                                        <li class="">
                                            <a class="S_txt2" title="评论"><span class="pos"><span class="line S_line1"><span><em class="W_ficon ficon_repeat S_ficon" id="comment_${item[i].id}" onclick="commentDisplay(${item[i].id},${item[i].id})"></em><em id="commentNum_${item[i].id}" class="commuentNum">${item[i].content_num != 0 ? item[i].content_num : "评论"}</em></span></span></span></a>
                                        </li>
                                        <li class="">
                                            <a class="S_txt2" title="赞"><span class="pos"><span class="line S_line1"><span class=""><em class="W_ficon ficon_praised S_txt2" id="thumb_${item[i].id}" onclick="thumb(${item[i].id},${item[i].id})">ñ</em><em id="likeNum_${item[i].id}" class="likeNum">${item[i].like != 0 ? item[i].like : "赞"}</em></span></span></span></a>
                                        </li>
                                    </ul>
                                </div>
                            </div>`);
                $(`#${item[i].id}`).append(`<div class="S_bg2 clearfix send_weibo_long commentBox"id="commentBox_${item[i].id}" node-type="wrap" style="display:none;">
                                <div class="sendCommentBox">
                                    <div class="commentPic">
                                    <img src="${headinComment}" alt="" height="50px" width="50px;" style="border-radius: 50%;">
                                </div>
                                <div class="input_option" style="display:inline-block; width:70%;">
                                    <div class="input" node-type="textElDiv" style="height: 20px; min-height: 20px;">
                                        <textarea class="W_input" id="sendComment_${item[i].id}" title="微博输入框" name="content" node-type="textEl" pic_split="1" range="0&0" style="overflow: hidden; height: 20px; min-height: 20px; width: 100%;" ></textarea>
                                        <div class="send_succpic send_succpicCom" id="sendSuc_${item[i].id}" style="display:none;" node-type="successTip"><span class="W_icon icon_succB"></span><span
                                            class="txt">发布评论成功</span>
                                        </div>
                                    </div>
                                    <div class="kind kind_comment">
                                        <a class="S_txt1" href="javascript:void(0);" action-type="multiimage" action-data="type=508&action=1&log=image&cate=1"
                                            title="图片" suda-uatrack="key=tblog_new_image_upload&value=image_button" style="position: relative;" onclick="fileFunc('image_file_${item[i].id}',${item[i].id})">
                                            <em class="W_ficon ficon_image">p</em>图片
                                            <div style="position: absolute; left: 0px; top: 0px; display: block; overflow: hidden; background-color: rgb(0, 0, 0); opacity: 0; width: 49px; height: 24px;">
                                                <input type="file" accept="image/jpg,image/jpeg,image/png,image/gif" hidefoucs="true" node-type="fileBtn"
                                                    name="pic1" style="cursor:pointer;width:1000px;height:1000px;position:absolute;bottom:0;right:0;font-size:200px;"
                                                    id="image_file_${item[i].id}" multiple="multiple">
                                            </div>
                                        </a>
                                        <a class="S_txt1" href="javascript:void(0);" action-type="video" action-data="type=502&action=1&log=video&cate=1"
                                            title="视频" suda-uatrack="key=tblog_home_edit&value=video_button" style="position: relative;" onclick="fileFunc('video_file_${item[i].id}',${item[i].id})">
                                            <em class="W_ficon ficon_video">q</em>视频<div style="position: absolute; left: 0px; top: 0px; display: block; overflow: hidden; background-color: rgb(0, 0, 0); opacity: 0; width: 49px; height: 24px;">
                                                <input type="file" name="video" hidefoucs="true" title="视频" accept=".mpg,.m4v,.mp4,.flv,.3gp,.mov,.avi,.rmvb,.mkv,.wmv"
                                                    style="cursor:pointer;width:1000px;height:1000px;position:absolute;bottom:0;right:0;font-size:200px;"
                                                    id="video_file_${item[i].id}">
                                            </div>
                                        </a>
                                        <div class="file_info file_info_com" id="file_info_${item[i].id}" style="display:inline-block"></div>
                                    </div>
                                </div>
                                <div class="sendCommentBtn">
                                        <a href="javascript:void(0);" class="W_btn_a btn_30px W_btn_a_disable" diss-data="module=stissue" type="submit"
                                            onclick="sendComment(${item[i].id})" title="发布评论按钮" id="publishBtn">发布评论</a>
                                </div>
                                </div>
                                <div class="commentViewBox" id="commentViewBox_${item[i].id}">
                                </div>
                            </div>`);
                $.ajax({
                    type: "POST",
                    url: "LikeServlet",
                    enctype: "multipart/form-data",
                    data: { "loadid": item[i].id },
                    async: false,
                    success: function (result) {
                        var thumb = document.getElementById(`thumb_${item[i].id}`);
                        if (result == "like") {
                            thumb.style.color = "#ee7700";
                            thumb.title = "取消赞";
                        }
                        else if (result == "cancel") {
                            thumb.style.color = "#808080";
                            thumb.title = "赞";
                        }
                    }
                })
                $.ajax({
                    type: "POST",
                    url: "FollowServlet",
                    enctype: "multipart/form-data",
                    async: false,
                    data: { "load_follow_name": `${item[i].user_nickname}` },
                    success: function (res) {
                        var following = document.getElementById(`followBtn_${item[i].id}`);
                        if (res == "follow") {
                            following.style.borderColor = "#ee7700"
                            following.style.backgroundColor = "#ee7700";
                            following.style.color = "#fff";
                            following.innerHTML = "已关注";
                            following.title = "取消关注";

                        }
                        else {
                            following.style.borderColor = "#808080";
                            following.style.backgroundColor = "#fff";
                            following.style.color = "buttontext";
                            following.innerHTML = "关注";
                            following.title = "关注";
                        }
                    }
                })
            }
            for (var i = 0; i < item.length; i++) {
                $(`#${item[i].id}`).animate({ left: `0px`, opacity: "1" }, 500);
            }
        }
    })
}