const message=document.getElementById('message');
async function api(path,method='GET',body,authenticated=false){
 const headers={}; if(body)headers['Content-Type']='application/json';
 if(authenticated)headers.Authorization='Bearer '+(sessionStorage.getItem('token')||'');
 const response=await fetch(path,{method,headers,body:body?JSON.stringify(body):undefined});
 const data=await response.json();
 if(!response.ok){if(authenticated&&response.status===401){sessionStorage.removeItem('token');location.href='/login';}throw new Error(data.message||'Yêu cầu thất bại');}
 return data;
}
for(const mode of ['login','register']){
 const form=document.getElementById(mode+'Form');
 if(form)form.addEventListener('submit',async e=>{
  e.preventDefault();const button=form.querySelector('button');button.disabled=true;message.textContent='Đang xử lý...';
  try{const data=await api('/auth/'+(mode==='login'?'login':'signup'),'POST',Object.fromEntries(new FormData(form)));
   if(mode==='login'){sessionStorage.setItem('token',data.token);location.href='/user/profile';}
   else{message.textContent='Đăng ký thành công. Hãy về trang đăng nhập.';form.reset();}
  }catch(err){message.textContent=err.message;}finally{button.disabled=false;}
 });
}
if(document.getElementById('profile')){
 if(!sessionStorage.getItem('token'))location.replace('/login');
 else api('/users/me','GET',null,true).then(u=>{document.getElementById('profile').textContent=`Họ tên: ${u.fullName}\nEmail: ${u.email}\nMã: ${u.id}`;}).catch(e=>message.textContent=e.message);
 document.getElementById('loadUsers').onclick=async()=>{try{const users=await api('/users/','GET',null,true);const box=document.getElementById('users');box.hidden=false;box.textContent=users.map(u=>`${u.id}. ${u.fullName} — ${u.email}`).join('\n');}catch(e){message.textContent=e.message;}};
 document.getElementById('logout').onclick=()=>{sessionStorage.removeItem('token');location.href='/login';};
}
