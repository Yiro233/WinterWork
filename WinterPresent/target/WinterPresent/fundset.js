function follow(id) {//关注用户
    var followNum = document.getElementById("follow");
    var followBtn = document.getElementById(`followBtn_${id}`);
    var user = document.getElementById(`nameInBox_${id}`).innerHTML;
    document.getElementById(`followBtn_${id}`).onmouseover = function () {
        if (followBtn.innerHTML == "已关注") return;
        document.getElementById(`followBtn_${id}`).style.cssText = `border: 1px solid #ee7700; color:#ee7700 `;
    }
    document.getElementById(`followBtn_${id}`).onmouseout = function () {
        if (followBtn.innerHTML == "已关注") return;
        document.getElementById(`followBtn_${id}`).style.cssText = `border: 1px solid #808080; color:buttontext; `;
    }
    $.ajax({
        type: "POST",
        url: "FollowServlet",
        enctype: "multipart/form-data",
        data: { "follow_name": user },
        success: function (res) {
            var following = document.getElementsByClassName(`followBtn_${user}`);
            if (followBtn.innerHTML == "已关注") {
                for (var i = 0; i < following.length; i++) {
                    following[i].style.cssText = `border: 1px solid #808080; background-color:#fff; color:buttontext; `;
                    following[i].innerHTML = "关注";
                    following[i].title = "关注";
                }
                followNum.innerText = Number(followNum.innerText) - 1;
            }
            else {
                for (var i = 0; i < following.length; i++) {
                    following[i].style.borderColor = "#ee7700"
                    following[i].style.backgroundColor = "#ee7700";
                    following[i].style.color = "#fff";
                    following[i].innerHTML = "已关注";
                    following[i].title = "取消关注";
                }
                followNum.innerText = Number(followNum.innerText) + 1;
            }
        }
    })
}
function showInfo(id) {//显示退出栏
    document.getElementById(id).style.display = "block";
}
function hideInfo(id) {//隐藏退出栏
    document.getElementById(id).style.display = "none";
}
function gotop() {//返回顶部
    var currentScroll = document.documentElement.scrollTop || document.body.scrollTop;
    if (currentScroll > 0) {
        window.requestAnimationFrame(gotop);
        window.scrollTo(0, currentScroll - (currentScroll / 5));
    }
}
function pictureView(i) {//查看大图
    var pic_large = document.getElementById("pic_large");
    var pic_view = document.getElementById("pic_view");
    var picCont;
    if (i == -1) picCont = document.getElementById("send_pic");
    else picCont = document.getElementById(`picCont_${i}`);
    pic_view.src = picCont.src;
    pic_large.style.display = "block";
}
function turnoff() {//关闭查看大图
    var pic_large = document.getElementById("pic_large");
    pic_large.style.display = "none";
}
window.addEventListener("scroll", function () {//监视网页滚动
    var goTopButn = document.getElementById("base_scrollToTop");
    var scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
    if (scrollTop > 180) {
        goTopButn.style.visibility = "visible";
    }
    else {
        goTopButn.style.visibility = "hidden";
    }
});
window.onload = load();
function load() {//加载个人信息
    $.ajax({
        type: "GET",
        url: "UserInfoServlet",
        enctype: "multipart/form-data",
        success: function (data) {
            var item = JSON.parse(data);
            var nickname = document.getElementById("nickname");
            var headpic = document.getElementById("headpic");
            var follow = document.getElementById("follow");
            var followed = document.getElementById("followed");
            var blog_num = document.getElementById("blog_num");
            var top_name = document.getElementById("top_name");
            nickname.innerHTML = item.nickname;
            nickname.title = item.nickname;
            headpic.src = item.avatar;
            headpic.title = item.nickname;
            follow.innerHTML = item.follow;
            followed.innerHTML = item.followed;
            blog_num.innerHTML = item.blog_num;
            top_name.innerHTML = item.nickname;
        }
    })
}
function layerDisplay(id) {//显示发布微博层
    var WB_layer = document.getElementById("WB_layer");
    if (id == 1){
        WB_layer.style.display = "block";
    }
    else WB_layer.style.display = "none";
}
function fileFunc(file,id) {//获取文件信息函数
    var fileInput = document.getElementById(file), info = document.getElementById(`file_info_${id}`);
    fileInput.addEventListener('change', function () {
        if (!fileInput.value) {
            info.innerText = '没有选择文件';
            return;
        }
        var file = fileInput.files[0];
        info.innerText = '文件: ' + file.name;
    }) 
}
function getFileUrl(file) {//获取文件URL
    if (!file) return;
    var url = null;
    if (window.createObjectURL != undefined) {
        url = window.createObjectURL(file);
    } else if (window.URL != undefined) { // mozilla(firefox)
        url = window.URL.createObjectURL(file);
    } else if (window.webkitURL != undefined) { // webkit or chrome
        url = window.webkitURL.createObjectURL(file);
    }
    return url;
}
function thumb(i, id) {//点赞功能
    var thumbed;
    var liknum;
    $.ajax({
        type: "POST",
        url: "LikeServlet",
        enctype: "multipart/form-data",
        data: { "likeid": id },
        success: function (isThumb) {
            if (i == -1) {
                thumbed = document.getElementById(`sendWBthumb`);
                liknum = document.getElementById(`sendWBthumb_num`);
            }
            else {
                thumbed = document.getElementById(`thumb_${i}`);
                liknum = document.getElementById(`likeNum_${i}`);
            }
            if (isThumb == "like") {
                thumbed.style.color = "#ee7700";
                thumbed.title = "取消赞";
                if (liknum.innerHTML == "赞") liknum.innerHTML = 1;
                else liknum.innerHTML = Number(liknum.innerHTML) + 1;
            }
            else if (isThumb == "cancel") {
                thumbed.style.color = "#808080";
                thumbed.title = "赞";
                if (liknum.innerHTML == 1) liknum.innerHTML = "赞";
                else liknum.innerHTML = Number(liknum.innerHTML) - 1;
            }
        }
    })
}
function collect(i, id) {//收藏功能
    var collected;
    var collectText;
    if (i == -1) {
        collected = document.getElementById(`sendWBcollect`);
        collectText = document.getElementById(`sendWBcollect_text`);
    }
    else {
        collected = document.getElementById(`collect_${i}`);
        collectText = document.getElementById(`collectText_${i}`);
    }
    $.ajax({
        type: "POST",
        url: "CollectServlet",
        enctype: "multipart/form-data",
        data: { "collect_id": id },
        success: function (isCollect) {
            if (collectText.innerText == "收藏") {
                collected.style.color = "#ee7700";
                collected.title = "取消收藏";
                collectText.innerHTML = "已收藏";
                if (document.getElementById(`collect_num`))
                    document.getElementById(`collect_num`).innerHTML = Number(document.getElementById(`collect_num`).innerHTML) + 1;
            }
            else if (collectText.innerText == "已收藏") {
                collected.style.color = "#808080";
                collected.title = "收藏";
                collectText.innerHTML = "收藏";
                if (document.getElementById(`collect_num`))
                    document.getElementById(`collect_num`).innerHTML = Number(document.getElementById(`collect_num`).innerHTML)-1;
            }
        }
    })
}
function now() {//获取当前时间
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
        + " :" + date.getHours() + seperator2 + date.getMinutes()
        + seperator2 + date.getSeconds();
    return currentdate;
}
function send(id) {//发送微博
    var textArea = document.getElementById(`sendWB_${id}`);
    var textCont = textArea.value;
    var sendSuc = document.getElementById(`sendSuc_${id}`);
    var nicknameInbox = document.getElementById("nickname").innerText;
    var userhead = document.getElementById("headpic").src;
    var pointer;
    if (id == "personInfo") {
        var WB_isEm = document.getElementById("WB_empty");
        pointer = document.getElementsByClassName("blob_box_info")[0] || WB_isEm;
        var WB_layer = document.getElementById("WB_layer");
    }
    else pointer = document.getElementById("pointer");
    var newBlog = document.createElement("div");
    var picContObj = $(`#image_file_${id}`).get(0).files[0];
    var videoObj = $(`#video_file_${id}`).get(0).files[0];
    var dateTime = now();
    var headinComment = document.getElementById("headpic").src;
    if ((!textCont) && (!picContObj) && (!videoObj)) alert("你还没有输入内容！")
    else {
        var formData = new FormData($('#sendWBForm')[0]);
        formData.set("pid", "0");
        formData.set("content", textCont);
        formData.set("picture", picContObj);
        formData.set("video", videoObj);
        $.ajax({
            type: "POST",
            url: "SendServlet",
            enctype: "multipart/form-data",
            data: formData,
            async: false,
            cache: false,
            processData: false,
            contentType: false,
            success: function (result) {
                var blog_number = document.getElementById("blog_num");
                blog_number.innerText = Number(blog_num.innerText) + 1;
                sendSuc.style.display = "block";
                textArea.value = "";
                setTimeout(() => {
                    sendSuc.style.display = "none";
                }, 500);
                if (id == "personInfo") {
                    WB_isEm.style.display = "none";
                    setTimeout(() => {
                        WB_layer.style.display = "none";
                    }, 500);
                }
                if (id == 2) setTimeout(() => {
                    sendSuc.style.display = "block";
                    layerDisplay();
                }, 500);
                var data = JSON.parse(result);
                if (id == "personInfo") {
                    newBlog.setAttribute("class", "blob_box_info");
                }
                else newBlog.setAttribute("class", "blob_box");
                newBlog.setAttribute("id", `${data.id}`);
                newBlog.style.display = "block";
                newBlog.innerHTML = `<div class="inner_detail">
                                        <div class="perInfo">
                                            <div class="headpic_box">
                                                <img src=${userhead} alt="" id="sendWBpic" class="headpic_inBox" width="50px;" height="50px;">
                                            </div>
                                            <div class="moreInfo">
                                                <div id="" class="nameInBox">${nicknameInbox}</div>
                                                <div id="" class="timeInBox">${dateTime}</div>
                                            </div>
                                        </div>
                                        <div class="blobCont">
                                            <div class="textCont" id="">${textCont}</div>
                                            <div class="picCont_box" style="display:${data.picture ? "block" : "none"}">
                                                <img src="${data.picture}" title="${data.picture ? "微博图片-点击查看大图" : ""}" id="send_pic" class="picCont" width="50%" onclick="pictureView(-1)">
                                            </div> 
                                            <div class="video_box" style="display:${data.video ? "block" : "none"}">
                                                <video src="${data.video}" controls="controls" width="50%"></video>                                
                                            </div>
                                        </div>
                                    </div>
                                    <div class="WB_handle">
                                        <ul class="WB_row_line WB_row_r4 clearfix S_line2">
                                            <li>
                                                 <a class="S_txt2"><span class="pos"><span class="line S_line1"><span><em class="W_ficon ficon_favorite S_ficon" id="sendWBcollect" onclick="collect(-1,${data.id})">û</em><em id="sendWBcollect_text" class="collectText">收藏</em></span></span></span></a>
                                            </li>
                                            <li>
                                                <a class="S_txt2"><span class="pos"><span class="line S_line1"><span><em class="W_ficon ficon_forward S_ficon"></em><em></em></span></span></span></a>
                                            </li>
                                            <li class="">
                                                <a class="S_txt2" title="评论"><span class="pos"><span class="line S_line1"><span><em class="W_ficon ficon_repeat S_ficon" id="comment_${data.id}" onclick="commentDisplay(${data.id},-1)"></em><em id="commentNum_${data.id}" class="commuentNum">评论</em></span></span></span></a>
                                            </li>
                                            <li class="">
                                                <a class="S_txt2" title="赞"><span class="pos"><span class="line S_line1"><span class=""><em class="W_ficon ficon_praised S_txt2" id="sendWBthumb" onclick="thumb(-1,${data.id})">ñ</em><em id="sendWBthumb_num" class="likeNum">赞</em></span></span></span></a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>`;
                if (id == "personInfo") {
                    pointer.parentNode.insertBefore(newBlog, pointer);
                    $(`#${data.id}`).animate({ right: `0px`, opacity: "1" }, 500);
                }
                else {
                    pointer.parentNode.insertBefore(newBlog, pointer.nextSibling);
                    $(`#${data.id}`).animate({ left: `0px`, opacity: "1" }, 500);
                }
                $(`#${data.id}`).append(`<div class="S_bg2 clearfix send_weibo_long commentBox"id="commentBox_${data.id}" node-type="wrap" style="display:none;">
                                <div class="sendCommentBox">
                                    <div class="commentPic">
                                    <img src="${headinComment}" alt="" height="50px" width="50px;" style="border-radius: 50%;">
                                </div>
                                <div class="input_option" style="display:inline-block; width:70%;">
                                    <div class="input" node-type="textElDiv" style="height: 20px; min-height: 20px;">
                                        <textarea class="W_input" id="sendComment_${data.id}" title="微博输入框" name="content" node-type="textEl" pic_split="1" range="0&0" style="overflow: hidden; height: 20px; min-height: 20px; width: 100%;" ></textarea>
                                        <div class="send_succpic send_succpicCom" id="sendSuc_${data.id}" style="display:none;" node-type="successTip"><span class="W_icon icon_succB"></span><span
                                            class="txt">发布评论成功</span>
                                        </div>
                                    </div>
                                    <div class="kind kind_comment">
                                        <a class="S_txt1" href="javascript:void(0);" action-type="multiimage" action-data="type=508&action=1&log=image&cate=1"
                                            title="图片" suda-uatrack="key=tblog_new_image_upload&value=image_button" style="position: relative;" onclick="fileFunc('image_file_${data.id}',${data.id})">
                                            <em class="W_ficon ficon_image">p</em>图片
                                            <div style="position: absolute; left: 0px; top: 0px; display: block; overflow: hidden; background-color: rgb(0, 0, 0); opacity: 0; width: 49px; height: 24px;">
                                                <input type="file" accept="image/jpg,image/jpeg,image/png,image/gif" hidefoucs="true" node-type="fileBtn"
                                                    name="pic1" style="cursor:pointer;width:1000px;height:1000px;position:absolute;bottom:0;right:0;font-size:200px;"
                                                    id="image_file_${data.id}" multiple="multiple">
                                            </div>
                                        </a>
                                        <a class="S_txt1" href="javascript:void(0);" action-type="video" action-data="type=502&action=1&log=video&cate=1"
                                            title="视频" suda-uatrack="key=tblog_home_edit&value=video_button" style="position: relative;" onclick="fileFunc('video_file_${data.id}',${data.id})">
                                            <em class="W_ficon ficon_video">q</em>视频<div style="position: absolute; left: 0px; top: 0px; display: block; overflow: hidden; background-color: rgb(0, 0, 0); opacity: 0; width: 49px; height: 24px;">
                                                <input type="file" name="video" hidefoucs="true" title="视频" accept=".mpg,.m4v,.mp4,.flv,.3gp,.mov,.avi,.rmvb,.mkv,.wmv"
                                                    style="cursor:pointer;width:1000px;height:1000px;position:absolute;bottom:0;right:0;font-size:200px;"
                                                    id="video_file_${data.id}">
                                            </div>
                                        </a>
                                        <div class="file_info file_info_com" id="file_info_${data.id}" style="display:inline-block"></div>
                                    </div>
                                </div>
                                <div class="sendCommentBtn">
                                        <a href="javascript:void(0);" class="W_btn_a btn_30px W_btn_a_disable" diss-data="module=stissue" type="submit"
                                            onclick="sendComment(${data.id})" title="发布评论按钮" id="publishBtn">发布评论</a>
                                </div>
                                </div>
                                <div class="commentViewBox" id="commentViewBox_${data.id}">
                                </div>
                            </div>`);
                document.getElementById(`image_file_${id}`).value = "";
                document.getElementById(`video_file_${id}`).value = "";
                document.getElementById(`file_info_${id}`).innerHTML = "";
            },
            error: function (error) {
                console.log(error);
            }
        })

    }
}


function commentDisplay(id, num) {//加载评论
    var commentBox = document.getElementById(`commentBox_${id}`);
    var nick = document.getElementById("nickname").innerText;
    if (commentBox.style.display == "none") {
        commentBox.style.display = "block";
        $(`#commentViewBox_${id}`).html(`<div class="pointerCommnet" id="pointer_${id}"></div>`);
        if (num != -1) {
            $.ajax({
                type: "GET",
                url: "InfoServlet",
                enctype: "multipart/form-data",
                success: function (res) {
                    var itemObj = JSON.parse(res);
                    var item = itemObj.data;
                    for(var i=0;i<item.length;i++){
                        if(num==item[i].id){
                            var comment = item[i].child;
                        }
                    }
                    if (comment.length != 0) {
                        for (var i = 0; i < comment.length; i++) {
                            $(`#commentViewBox_${id}`).append(`<div class="blob_box" id="${comment[i].id}" style="display: block; left:0px;opacity:1;">
                                <div class="inner_detail">
                                    <div class="perInfo">
                                        <div class="headpic_box">
                                            <img src="${comment[i].avatar}" alt="${comment[i].user_nickname}" id="headpic_inBox" class="headpic_inBox" width="50px;" height="50px;">
                                        </div>
                                        <div class="moreInfo moreInfoCom">
                                            <div class="nameInBox">
                                                <span id="nameInBox_${comment[i].id}">${comment[i].user_nickname}</span>
                                                <button class="followBtn followBtn_${comment[i].user_nickname}" id="followBtn_${comment[i].id}" title="关注"  onclick="follow(${comment[i].id})" style="display: ${comment[i].user_nickname==nick?"none":"block"};">关注</button>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="blobCont blobContCom">
                                        <div class="textCont" id="textCont_${comment[i].id}">${comment[i].content}</div>
                                        <div class="picCont_box" style="display:${comment[i].pic ? "block" : "none"}">
                                            <a><img src="${comment[i].pic}" id="picCont_${comment[i].id}" class="picCont" width="50%" onclick="pictureView(${comment[i].id})"></a>
                                        </div>
                                        <div class="video_box" style="display:${comment[i].video ? "block" : "none"}">
                                            <video src="${comment[i].video}" controls="controls" width="50%"></video>                                
                                        </div>
                                    </div>
                                    <div id="timeInBox_${comment[i].id}" class="timeInBox timeInBoxCom" style="display:inline-block">
                                        ${comment[i].date}
                                        <ul style="display:inline-block" class="thumbCom">
                                            <li>
                                                <a class="S_txt2" title="赞"><span class="pos"><span class="line S_line1"><span class=""><em class="W_ficon ficon_praised S_txt2" id="thumb_${comment[i].id}" onclick="thumb(${comment[i].id},${comment[i].id})">ñ</em><em id="likeNum_${comment[i].id}" class="likeNum">${comment[i].like != 0 ? comment[i].like : "赞"}</em></span></span></span></a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>`);
                            $.ajax({
                                type: "POST",
                                url: "LikeServlet",
                                enctype: "multipart/form-data",
                                data: { "loadid": comment[i].id },
                                async: false,
                                success: function (result) {
                                    var thumb = document.getElementById(`thumb_${comment[i].id}`);
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
                                data: { "load_follow_name": `${comment[i].user_nickname}` },
                                success: function (res) {
                                    var following = document.getElementById(`followBtn_${comment[i].id}`);
                                    if (res == "follow") {
                                        following.style.borderColor = "#ee7700"
                                        following.style.backgroundColor = "#ee7700";
                                        following.style.color = "#fff";
                                        following.innerHTML = "已关注";
                                        following.title = "取消关注";

                                    }
                                    else {
                                        following.innerHTML = "关注";
                                        following.title = "关注";
                                    }
                                }
                            })
                        }
                    }
                }
            });
        }
        else return;
    }
    else commentBox.style.display = "none";
}
function sendComment(pid) {//发送评论
    var commentArea = document.getElementById(`sendComment_${pid}`);
    var commentCont = commentArea.value;
    var sendSucCom = document.getElementById(`sendSuc_${pid}`);
    var nicknameInbox = document.getElementById("nickname").innerText;
    var userhead = document.getElementById("headpic").src;
    var pointer = document.getElementById(`pointer_${pid}`);
    var newBlog = document.createElement("div");
    var picContObj = $(`#image_file_${pid}`).get(0).files[0];
    var videoObj = $(`#video_file_${pid}`).get(0).files[0];
    var comment_num = document.getElementById(`commentNum_${pid}`);
    var dateTime = now();
    if ((!commentCont) && (!picContObj) && (!videoObj)) alert("你还没有输入内容！")
    else {
        var formData = new FormData($('#sendWBForm')[0]);
        formData.set("pid", pid);
        formData.set("content", commentCont);
        formData.set("picture", picContObj);
        formData.set("video", videoObj);
        $.ajax({
            type: "POST",
            url: "SendServlet",
            enctype: "multipart/form-data",
            data: formData,
            async: false,
            cache: false,
            processData: false,
            contentType: false,
            success: function (result) {
                sendSucCom.style.display = "block";
                commentArea.value = "";
                setTimeout(() => {
                    sendSucCom.style.display = "none";
                }, 500);
                var data = JSON.parse(result);
                newBlog.setAttribute("class", "blob_box");
                newBlog.setAttribute("id", `${data.id}`);
                newBlog.style.display = "block";
                newBlog.innerHTML = `<div class="inner_detail">
                                        <div class="perInfo">
                                            <div class="headpic_box">
                                                <img src=${userhead} alt="" id="sendWBpic" class="headpic_inBox" width="50px;" height="50px;">
                                            </div>
                                            <div class="moreInfo moreInfoCom">
                                                <div id="nameInBox_${data.id}" class="nameInBox">${nicknameInbox}</div>
                                            </div>
                                        </div>
                                        <div class="blobCont blobContCom">
                                            <div class="textCont" id="textCont_${data.id}">${commentCont}</div>
                                            <div class="picCont_box" style="display:${data.picture ? " block" : "none"}">
                                                <img src="${data.picture}" title="${data.picture ? " 微博图片-点击查看大图" : ""}" id="send_pic" class="picCont"
                                                    width="50%" onclick="pictureView(-1)">
                                            </div>
                                            <div class="video_box" style="display:${data.video ? " block" : "none"}">
                                                <video src="${data.video}" controls="controls" width="50%"></video>
                                            </div>
                                        </div>
                                        <div id="timeInBox_${data.id}" class="timeInBox timeInBoxCom" style="display:inline-block">
                                            ${dateTime}
                                            <ul style="display:inline-block" class="thumbCom">
                                                <li>
                                                    <a class="S_txt2" title="赞"><span class="pos"><span class="line S_line1"><span class=""><em class="W_ficon ficon_praised S_txt2"
                                                                        id="thumb_${data.id}" onclick="thumb(${data.id},${data.id})">ñ</em><em id="likeNum_${data.id}"
                                                                        class="likeNum">赞</em></span></span></span></a>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>`;
                pointer.parentNode.insertBefore(newBlog, pointer.nextSibling);
                if (comment_num.innerText == "评论") comment_num.innerHTML = 1;
                else comment_num.innerHTML = Number(comment_num.innerHTML)+1;
                $(`#${data.id}`).animate({ left: `0px`, opacity: "1" }, 500);
                document.getElementById(`image_file_${pid}`).value = "";
                document.getElementById(`video_file_${pid}`).value = "";
                document.getElementById(`file_info_${pid}`).innerHTML = "";
            },
            error: function (error) {
                console.log(error);
            }
        })

    }
}