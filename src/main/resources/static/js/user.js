let index={
	init: function(){
		$("#btn-save").on("click",()=>{
			this.save();			
		});	
		
		$("#btn-update").on("click",()=>{
			this.update();			
		});	
	},
		
	save: function(){
		//alert('user의 save함수 호출됨');
		let data={
			username:$("#username").val(),
			password:$("#password").val(),
			email:$("#email").val(),
		};		
		//console.log(data);
		//ajax호출시 디폴트가 비동기호출이다.
		//ajax통신을 이용해서 상기3개의 데이터를
		//json으로 변경하여 insert요청	
		//ajax이 통신을 성공하고 서버가 json을 리턴해주면 자동으로 자바 오브젝트로 변환해주네요!
		$.ajax({//$.ajax임에 유의 ($ 다음에 점(.)을 빼먹지 말자)
			type:"POST",
			url:"/auth/join",
			data:JSON.stringify(data),//http body데이터임
			contentType:"application/json; charset=utf-8",
			dataType:"json"//요청을 서버로해서 응답이 왔을때 기본적으로 모든것은 문자열이며 그 생긴 문자열 구성이 json이라면 javascrip오브젝트로 자동 변경해줌
		}).done(function(resp){//성공일때 실행함수
			//alert("회원가입이 완료되었습니다.");
			//console.log(resp);
			if(resp.status==500){
				alert("회원가입에 실패하였습니다!");
			}else{
				alert("회원가입에 성공하였습니다!");
			    location.href="/";
			}
			
		}).fail(function(error){//실패일때 실행함수
			alert(JSON.stringify(error));
		});
	},
	
	update: function(){		
		let data={
			id:$("#id").val(),
			username:$("#username").val(),
			password:$("#password").val(),
			email:$("#email").val(),
		};	
		$.ajax({
			type:"PUT",
			url:"/user",
			data:JSON.stringify(data),
			contentType:"application/json; charset=utf-8",
			dataType:"json"
		}).done(function(resp){
			alert("회원수정이 완료되었습니다.");			
			location.href="/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	}      
	
}

index.init();
