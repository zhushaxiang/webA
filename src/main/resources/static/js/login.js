$(function(){
	$('#login').click(function(){
		var user = new Object();
		user.username=$('#username').val();
		user.pwd = $('#userpwd').val();
		$.post("/userInfo/validate",user,function(data){
			console.log(data);
			if(data == "true"){
				alert('登陆成功！');
			}else{
				alert('用户名或者密码错误！');
			}
		});
	});
});