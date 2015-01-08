var mysql = require('mysql');
var client = mysql.createConnection({
 host:"localhost",
 user:"root",
 password:"5074",
 port : 3307,
 database : "touch"
});

client.connect();

//tcp server. register for DB and to find random target in DB 
var DBServer = require('net').createServer();
DBServer.on('listening', function(){
 console.log('DB Server port is :', 3307);
});

//socket array
var sockets = [];
var query;
DBServer.on('connection', function(socket){
 console.log('DB connection');
 client.query('USE touch');

 socket.on('data', function(data){
  var str = data.toString();
  console.log(str);
  
  //register user
  if(str[0] === 's'){
	  var strArr = str.split('+');
	  var nickName = strArr[1];
	  var ID = strArr[2];
	  var PW = strArr[3];
	  var age = strArr[4];
	  var sex = strArr[5];
	  
	  console.log(nickName);
	  console.log(ID);
	  console.log(PW);
	  console.log(sex);
	  console.log(age);
	  
	  
      query = 'insert into touch.member_info values('+'\''+ID+'\', hex(aes_encrypt(\''+ID+'\',\''+PW+'\')), '+'\''+nickName+'\','+age+', '+sex+');';
	  console.log(query);
	  client.query(query, function(error,result,fields){
		  if(error){
			  console.log('simple registration failed');
			  socket.write('f');
			  }else{
				  console.log('simple registration success');
				  socket.write('s');
				  }
		    
		   });
  }
  else if(str[0] === 'l'){//login user
	  var str = data.toString();
	  console.log(str);
	  
	  var strArr = str.split('+');
	  var ID = strArr[1];
	  //console.log(ID);
	  var PW = strArr[2];
	  //console.log(PW);
	  var nickName;
	  var isvisited = [];
	  var lastIndex;
	  var visitList='';
	  var sflag=false;
	  query='SELECT distinct user_id, tagSpot from touch.taggingtable where user_id=\''+ID+'\';';
	  //SELECT distinct user_id, tagSpot from touch.taggingtable where user_id='admin@admin.com';
	  console.log(query);
	  client.query(query,function(error,result,fields){
		  if(result===undefined){
			  sflag=false;
		  }
		  else{
			  sflag=true;
		  for(var i in result)
			  {
			  	isvisited[i]=result[i].tagSpot;
			  	//console.log('isvisited[] : ' +i +" " + result[i].tagSpot);
			  	//console.log(result[i].tagSpot);
			  	lastIndex=i;
			  	visitList=visitList+result[i].tagSpot;
			  	console.log(visitList);
			  }
		  }
	  });
	  
	  query='select user_id,nickName from touch.member_info where passwd=hex(aes_encrypt(\''+ID+'\''+',\''+PW+'\'));';
	  //query='SELECT * FROM member_info m;';
	  
	  console.log(query);
	  //client.query("USE touch");
	  
	  client.query(query, function(error,result,fields){
		    if(error){
		     console.log('Login failed');
		     socket.write('f');
		    }
		    else{
		    	
		      console.log(result);
              var gadget = result[0];
              //console.log(gadget);
              //console.log(rows[0].user_id);
              if(gadget===undefined)
            	  {
            	  //id를 검색해서 없으면 아이디가 없습니다.
            	  	console.log('Please check ID or PASSWORD');
            	  	//console.log(fields);
            	  	socket.write('f');
            	  }
              else if(gadget.user_id===ID){
            	 console.log('Login success');
            	 //console.log('what i pass '+'s'+nickName );
            	 if(lastIndex===undefined){
            		 console.log('s'+","+gadget.nickName);
            		 socket.write('s'+","+gadget.nickName+",f");
            	 }
            	 else{
            		 console.log('s'+","+gadget.nickName+","+lastIndex+","+visitList);
         		     socket.write('s'+","+gadget.nickName+","+lastIndex+","+visitList);
            		
            	 }
     		     //console.log('what i pass to : ' + 's'+gadget.nickName);
              }
       
           }
		    
		   });
		  
  }else if(str[0] === 'n'){//when you tag nfc
	  var str = data.toString();
	  console.log(str);
	  
	  var strArr = str.split('+');
	  var ID = strArr[1];
	  //console.log(ID);
	  var tagSpot = strArr[2];
	  //console.log(PW);
	  var tagTime = strArr[3];
	  var userCnt;
	  //[ { 'count(user_id)': 43 } ]
	  //insert into touch.taggingtable(user_id,tagSpot,tagTime) values('admin@admin.com','3', '2014.6.28 23:52:33');
	  query = 'insert into touch.taggingtable(user_id,tagSpot,tagTime) values('+'\''+ID+'\','+'\''+tagSpot+'\''+', '+'\''+tagTime+'\''+');';
	  console.log(query);
	  client.query(query);
	
	  query = 'select count(user_id) as user_id from touch.taggingtable where '+'tagSpot=\''+tagSpot+'\';';
	  console.log(query);
	client.query(query,function(error,result,fields){userCnt = result[0].user_id;});
	
	query = 'select count(user_id) as user_id from touch.taggingtable where tagSpot=\''+tagSpot+'\' AND user_id=\''+ID+'\';';
	console.log(query);
		//select count(user_id) as user_id from touch.taggingtable where tagSpot='3' AND user_id='admin7777@admin.com';
	  client.query(query, function(error,result,fields){
		  if(error){
			  console.log('Tag info not saved at taggingtable');
			  socket.write('f');
			  }else{
				  console.log('Tag info save success');
				  console.log(result[0]);
				  console.log('s'+","+userCnt+","+result[0].user_id);
				socket.write('s'+","+userCnt+","+result[0].user_id);
			}
		});
  }else if(str[0] === 'p'){//s'p'ot activity
	  var str = data.toString();
	  console.log(str);
	  
	  var strArr = str.split('+');
	  var ID = strArr[1];
	  //console.log(ID);
	  var tagSpot = strArr[2];
	  //console.log(PW);
	  var tagTime = strArr[3];
	  var userCnt;
	  var mostVisitId;
	  var mostuserCnt;
	  //[ { 'count(user_id)': 43 } ]
	  //insert into touch.taggingtable(user_id,tagSpot,tagTime) values('admin@admin.com','3', '2014.6.28 23:52:33');
//	  query = 'insert into touch.taggingtable(user_id,tagSpot,tagTime) values('+'\''+ID+'\','+'\''+tagSpot+'\''+', '+'\''+tagTime+'\''+');';
//	  console.log(query);
//	  client.query(query);
	
	  query = 'select count(user_id) as user_id from touch.taggingtable where '+'tagSpot=\''+tagSpot+'\';';
	  console.log(query);
	client.query(query,function(error,result,fields){userCnt = result[0].user_id;});
	console.log(userCnt);	
	
	query = 'select user_id, count(user_id) as visitCount from touch.taggingtable where tagSpot=\''+tagSpot+'\' group by user_id order by visitCount desc;';
	console.log(query);
	//client.query(query,function(error,result,fields){console.log(result)});
	client.query(query,function(error,result,fields){
			if(result[0]===undefined)
				socket.write('q');
			else{
				mostuserCnt = result[0].visitCount; mostVisitId = result[0].user_id;
			}
				

		});
		
	//select user_id, count(user_id) as visitCount from touch.taggingtable where tagSpot='2' group by user_id order by visitCount desc;
	query = 'select key_id from touch.taggingtable order by key_id DESC;'
	  client.query(query, function(error,result,fields){
		  if(error){
			  console.log('Tag info not saved at taggingtable');
			  socket.write('f');
			  }else{
				  console.log('Tag info save success');
				  console.log(result[0]);
				  console.log('s'+","+userCnt+","+result[0].user_id+","+mostVisitId+","+mostuserCnt);
					socket.write('s'+","+userCnt+","+result[0].user_id+","+mostVisitId+","+mostuserCnt);
			}
		});
	  
	
		  
  }
  
  
 });

 socket.on('close', function(){
  console.log('socket connection close');
  var index = sockets.indexOf(socket);
  if(index !== -1){
   sockets.splice(index, 1);
  }
 });
 
 socket.on('error', function(err){
  if(err){
   console.log('DB server socket ' + err);
  }
 });
});
DBServer.on('close', function(){
 console.log('DB close');
});

DBServer.on('error', function(err){
 console.log('DB server error:', err.message);
});
DBServer.listen(4002);

//SELECT distinct user_id, tagSpot from touch.taggingtable where user_id='admin@admin.com';
