let index={
	init: function(){
		$("#btn-save").on("click",()=>{
			this.save();			
		});	
		
			$("#btn-delete").on("click",()=>{
			this.deleteById();			
		});	
		
		$("#btn-update").on("click",()=>{
			this.update();			
		});	
		
		$("#btn-reply-save").on("click",()=>{
			this.replySave();			
		});	
		
		 //아래 replyDelete: function(boardId, replyId) 구동은 jsp에서 <a> 테크를 사용하므로 여기 클릭 함수 설정이 필요 없음
	},
		
	save: function(){		
		let data={
			title:$("#title").val(),
			content:$("#content").val()
			
		};			
		$.ajax({//$.ajax임에 유의 ($ 다음에 점(.)을 빼먹지 말자)
			type:"POST",
			url:"/api/board",
			data:JSON.stringify(data),
			contentType:"application/json; charset=utf-8",
			dataType:"json"
		}).done(function(resp){
			alert("글쓰기가 완료되었습니다.");
			location.href="/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	},   	
	
	deleteById: function(){	
		let id=$("#id").text();				
		
		$.ajax({
		   type:"DELETE",
			url:"/api/board/"+id,
			dataType:"json",
			contentType: 'application/json; charset=utf-8'
		}).done(function(resp){
			alert("삭제가 완료되었습니다.");
			location.href="/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	},
	
	update: function(){	
		
		let id=$("#id").val();
			
		let data={
			title:$("#title").val(),
			content:$("#content").val()
			
		};			
		$.ajax({//$.ajax임에 유의 ($ 다음에 점(.)을 빼먹지 말자)
			type:"PUT",
			url:"/api/board/"+id,
			data:JSON.stringify(data),
			contentType:"application/json; charset=utf-8",
			dataType:"json"
		}).done(function(resp){
			alert("글수정이 완료되었습니다.");
			location.href="/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	},
	
	replySave: function(){		
		let data={
			userid:$("#userId").val(),
			content:$("#reply-content").val(),	
			boardId:$("#boardId").val()		
		};		
	    	
	$.ajax({
			type: "POST",
			url: "/api/board/"+data.boardId+"/reply",			
			data:JSON.stringify(data),
			contentType:"application/json; charset=utf-8",
			dataType:"json"
		}).done(function(resp){
			alert("댓글 작성이 완료되었습니다.");
			location.href="/board/"+data.boardId;
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	} ,
	
	replyDelete: function(boardId, replyId){	
		//boardId와 replyId를 Number()로 감싸주시면 int형으로 바뀌고 그 뒤에 ajax를 타게 만들면 됩니다!	
		//alert("/api/board/"+boardId+"/reply/"+replyId);
	$.ajax({
			type: "DELETE",
			url: "/api/board/"+boardId+"/reply/"+replyId,			
			dataType:"json"
		}).done(function(resp){
			alert("댓글  삭제 성공");
			location.href="/board/"+boardId;
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	}  
}

index.init();
