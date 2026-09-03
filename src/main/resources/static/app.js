const API_URL = '/api';

// Auth logic
const loginForm = document.getElementById('login-form');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const email = document.getElementById('login-email').value;
        const password = document.getElementById('login-password').value;
        const errorElem = document.getElementById('login-error');
        
        try {
            const res = await fetch(`${API_URL}/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });
            const data = await res.json();
            console.log("data received", data);
            
            if (res.ok) {
                localStorage.setItem('user', JSON.stringify(data));
                if (data.role === 'HR') {
                    window.location.href = 'hr.html';
                } else {
                    window.location.href = 'employee.html';
                }
            } else {
                errorElem.innerText = data.error || 'Login failed';
            }
        } catch (err) {
            errorElem.innerText = 'Server error';
        }
    });
}

const registerForm = document.getElementById('register-form');
if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const name = document.getElementById('reg-name').value;
        const email = document.getElementById('reg-email').value;
        const password = document.getElementById('reg-password').value;
        const role = document.getElementById('reg-role').value;
        const errorElem = document.getElementById('reg-error');
        const successElem = document.getElementById('reg-success');
        
        try {
            const res = await fetch(`${API_URL}/auth/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name, email, password, role })
            });
            const data = await res.json();
            
            if (res.ok) {
                successElem.innerText = 'Registration successful! Please login.';
                errorElem.innerText = '';
                registerForm.reset();
            } else {
                errorElem.innerText = data.error || 'Registration failed';
                successElem.innerText = '';
            }
        } catch (err) {
            errorElem.innerText = 'Server error';
        }
    });
}

function logout() {
    localStorage.removeItem('user');
    window.location.href = 'index.html';
}

// Employee Dashboard Logic
async function loadEmployeeData(userId) {
    // Load attendance
    try {
        const res = await fetch(`${API_URL}/attendance/user/${userId}`);
        if (res.ok) {
            const logs = await res.json();
            const tbody = document.querySelector('#att-table tbody');
            tbody.innerHTML = '';
            logs.forEach(log => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${log.date}</td>
                    <td>${log.checkInTime || '-'}</td>
                    <td>${log.checkOutTime || '-'}</td>
                    <td>${log.hoursWorked || '-'}</td>
                `;
                tbody.appendChild(tr);
            });
        }
    } catch(err) { console.error(err); }

    // Load leave requests
    try {
        const res = await fetch(`${API_URL}/leave/user/${userId}`);
        if (res.ok) {
            const leaves = await res.json();
            const tbody = document.querySelector('#leave-table tbody');
            tbody.innerHTML = '';
            leaves.forEach(req => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${req.startDate}</td>
                    <td>${req.endDate}</td>
                    <td>${req.daysRequested}</td>
                    <td>${req.status}</td>
                `;
                tbody.appendChild(tr);
            });
        }
    } catch(err) { console.error(err); }
}

async function checkIn() {
    const user = JSON.parse(localStorage.getItem('user'));
    const msgElem = document.getElementById('att-message');
    try {
        const res = await fetch(`${API_URL}/attendance/check-in/${user.id}`, { method: 'POST' });
        const data = await res.json();
        if (res.ok) {
            msgElem.innerText = 'Checked in successfully!';
            msgElem.className = 'success';
            loadEmployeeData(user.id);
        } else {
            msgElem.innerText = data.error;
            msgElem.className = 'error';
        }
    } catch(err) { console.error(err); }
}

async function checkOut() {
    const user = JSON.parse(localStorage.getItem('user'));
    const msgElem = document.getElementById('att-message');
    try {
        const res = await fetch(`${API_URL}/attendance/check-out/${user.id}`, { method: 'POST' });
        const data = await res.json();
        if (res.ok) {
            msgElem.innerText = 'Checked out successfully!';
            msgElem.className = 'success';
            loadEmployeeData(user.id);
        } else {
            msgElem.innerText = data.error;
            msgElem.className = 'error';
        }
    } catch(err) { console.error(err); }
}

const leaveForm = document.getElementById('leave-form');
if (leaveForm) {
    leaveForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const user = JSON.parse(localStorage.getItem('user'));
        const startDate = document.getElementById('leave-start').value;
        const endDate = document.getElementById('leave-end').value;
        const msgElem = document.getElementById('leave-message');
        
        try {
            const res = await fetch(`${API_URL}/leave/apply/${user.id}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ startDate, endDate })
            });
            const data = await res.json();
            if (res.ok) {
                msgElem.innerText = 'Leave requested successfully!';
                msgElem.className = 'success';
                leaveForm.reset();
                loadEmployeeData(user.id);
            } else {
                msgElem.innerText = data.error;
                msgElem.className = 'error';
            }
        } catch(err) { console.error(err); }
    });
}

// HR Dashboard Logic
async function loadHRData() {
    const user = JSON.parse(localStorage.getItem('user'));
    const headers = { 'X-User-Id': user.id };

    // Load all employees
    try {
        const res = await fetch(`${API_URL}/auth/employees`, { headers });
        if (res.ok) {
            const emps = await res.json();
            const tbody = document.querySelector('#hr-emp-table tbody');
            tbody.innerHTML = '';
            emps.forEach(emp => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${emp.id}</td>
                    <td>${emp.name}</td>
                    <td>${emp.email}</td>
                    <td>${emp.leaveBalance}</td>
                `;
                tbody.appendChild(tr);
            });
        }
    } catch(err) { console.error(err); }

    // Load all attendance
    try {
        const res = await fetch(`${API_URL}/attendance/all`, { headers });
        if (res.ok) {
            const logs = await res.json();
            const tbody = document.querySelector('#hr-att-table tbody');
            tbody.innerHTML = '';
            logs.forEach(log => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${log.user.id} - ${log.user.name}</td>
                    <td>${log.date}</td>
                    <td>${log.checkInTime || '-'}</td>
                    <td>${log.checkOutTime || '-'}</td>
                    <td>${log.hoursWorked || '-'}</td>
                `;
                tbody.appendChild(tr);
            });
        }
    } catch(err) { console.error(err); }

    // Load all leave requests
    try {
        const res = await fetch(`${API_URL}/leave/all`, { headers });
        if (res.ok) {
            const leaves = await res.json();
            const tbody = document.querySelector('#hr-leave-table tbody');
            tbody.innerHTML = '';
            leaves.forEach(req => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${req.user.id} - ${req.user.name}</td>
                    <td>${req.startDate}</td>
                    <td>${req.endDate}</td>
                    <td>${req.daysRequested}</td>
                    <td>${req.status}</td>
                    <td>
                        ${req.status === 'PENDING' ? `
                            <button onclick="processLeave(${req.id}, 'APPROVED')" style="background:green">Approve</button>
                            <button onclick="processLeave(${req.id}, 'REJECTED')" style="background:red">Reject</button>
                        ` : '-'}
                    </td>
                `;
                tbody.appendChild(tr);
            });
        }
    } catch(err) { console.error(err); }
}

async function processLeave(requestId, status) {
    const user = JSON.parse(localStorage.getItem('user'));
    try {
        const res = await fetch(`${API_URL}/leave/process/${requestId}`, {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                'X-User-Id': user.id
            },
            body: JSON.stringify({ status })
        });
        if (res.ok) {
            alert('Leave request updated to ' + status);
            loadHRData(); // refresh table
        } else {
            const data = await res.json();
            alert('Error: ' + data.error);
        }
    } catch(err) { console.error(err); }
}
