// Global State
let currentUser = null;
let currentUserType = null; // 'patient' or 'admin'

// Page Navigation
function showPage(pageId) {
    document.querySelectorAll('.page').forEach(page => page.classList.remove('active'));
    const page = document.getElementById(pageId);
    if (page) {
        page.classList.add('active');
    }
}

function showHome() {
    currentUser = null;
    currentUserType = null;
    showPage('homePage');
    updateNavbar();
}

function showAbout() {
    showPage('aboutPage');
}

function showPatientLogin() {
    showPage('patientLoginPage');
}

function showPatientRegister() {
    showPage('patientRegisterPage');
}

function showAdminLogin() {
    showPage('adminLoginPage');
}

function showPatientDashboard() {
    showPage('patientDashboard');
    showPatientAppointmentRequest();
}

function showAdminDashboard() {
    showPage('adminDashboard');
    loadPendingRequests();
}

// Content Sections
function showSection(sectionId) {
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    });
    document.querySelectorAll('.menu-item').forEach(item => {
        item.classList.remove('active');
    });

    const section = document.getElementById(sectionId);
    if (section) {
        section.classList.add('active');
    }

    // Mark corresponding menu item as active
    const activeMenu = document.querySelector(`.menu-item[onclick*="${sectionId.replace('Section', '')}"]`);
    if (activeMenu) {
        activeMenu.classList.add('active');
    }
}

function showPatientAppointmentRequest() {
    showSection('appointmentRequestSection');
}

function showPatientAppointments() {
    showSection('appointmentsSection');
    loadPatientAppointments();
}

function showPatientProfile() {
    showSection('profileSection');
    loadPatientProfile();
}

function showPendingRequests() {
    showSection('pendingRequestsSection');
    loadPendingRequests();
}

function showDoctorList() {
    showSection('doctorManagementSection');
    loadDoctorList();
}

function showAddDoctor() {
    showSection('addDoctorSection');
}

function showAllAppointments() {
    showSection('allAppointmentsSection');
    loadAllAppointments();
}

// Patient Functions
async function handlePatientRegister(event) {
    event.preventDefault();

    const data = {
        name: document.getElementById('regName').value,
        email: document.getElementById('regEmail').value,
        phone: document.getElementById('regPhone').value,
        age: parseInt(document.getElementById('regAge').value),
        gender: document.getElementById('regGender').value,
        address: document.getElementById('regAddress').value
    };

    try {
        const patient = API.registerPatient(data);
        showToast('Registration successful! Please login.', 'success');
        document.getElementById('patientRegisterForm').reset();
        setTimeout(() => showPatientLogin(), 1500);
    } catch (error) {
        showToast('Registration failed: ' + error.message, 'error');
    }
}

async function handlePatientLogin(event) {
    event.preventDefault();

    const email = document.getElementById('patientEmail').value;

    try {
        const patient = API.loginPatient(email);
        currentUser = patient;
        currentUserType = 'patient';

        document.getElementById('patientName').textContent = patient.name;
        document.getElementById('patientEmail').textContent = patient.email;

        showToast('Login successful!', 'success');
        updateNavbar();
        showPatientDashboard();
    } catch (error) {
        showToast('Login failed: ' + error.message, 'error');
    }
}

async function handleAppointmentRequest(event) {
    event.preventDefault();

    const data = {
        patientId: currentUser.patientId,
        specialization: document.getElementById('specialization').value,
        requestedDate: document.getElementById('appointmentDate').value,
        description: document.getElementById('description').value
    };

    try {
        const appointment = API.requestAppointment(data);
        showToast('Appointment request submitted successfully!', 'success');
        document.getElementById('appointmentForm').reset();
        setTimeout(() => showPatientAppointments(), 1500);
    } catch (error) {
        showToast('Failed to request appointment: ' + error.message, 'error');
    }
}

async function loadPatientAppointments() {
    try {
        const requests = API.getAppointmentRequests(currentUser.patientId);
        const appointments = API.getAppointmentsByPatientId(currentUser.patientId);

        let html = '';

        // Appointment Requests
        if (requests.length > 0) {
            html += '<h3>Pending Requests</h3>';
            requests.forEach(req => {
                html += `
                    <div class="request-card">
                        <div class="card-header">
                            <div class="card-title">Requested Appointment</div>
                            <span class="card-badge badge-${req.status.toLowerCase()}">${req.status}</span>
                        </div>
                        <div class="card-info">
                            <div class="info-item">
                                <span class="info-label">Specialization</span>
                                <span class="info-value">${req.specialization}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Requested Date</span>
                                <span class="info-value">${new Date(req.requestedDate).toLocaleDateString()}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Status</span>
                                <span class="info-value">${req.status}</span>
                            </div>
                        </div>
                        ${req.description ? `<p><strong>Description:</strong> ${req.description}</p>` : ''}
                    </div>
                `;
            });
        }

        // Confirmed Appointments
        if (appointments.length > 0) {
            html += '<h3 style="margin-top: 2rem;">Confirmed Appointments</h3>';
            appointments.forEach(appt => {
                const doctor = API.doctors.find(d => d.doctorId === appt.doctorId);
                html += `
                    <div class="appointment-card">
                        <div class="card-header">
                            <div class="card-title">${doctor ? doctor.name : 'Doctor'}</div>
                            <span class="card-badge badge-confirmed">${appt.status}</span>
                        </div>
                        <div class="card-info">
                            <div class="info-item">
                                <span class="info-label">Specialization</span>
                                <span class="info-value">${doctor ? doctor.specialization : 'N/A'}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Date & Time</span>
                                <span class="info-value">${new Date(appt.appointmentDate).toLocaleString()}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Status</span>
                                <span class="info-value">${appt.status}</span>
                            </div>
                        </div>
                    </div>
                `;
            });
        }

        const appointmentsList = document.getElementById('appointmentsList');
        appointmentsList.innerHTML = html || '<p class="empty-state">No appointments yet. <a href="#" onclick="showPatientAppointmentRequest()">Request one now</a></p>';
    } catch (error) {
        showToast('Failed to load appointments: ' + error.message, 'error');
    }
}

async function loadPatientProfile() {
    try {
        const profileInfo = document.getElementById('profileInfo');
        const html = `
            <div class="profile-section">
                <h3>Personal Information</h3>
                <div class="profile-field">
                    <span class="profile-label">Name:</span>
                    <span class="profile-value">${currentUser.name}</span>
                </div>
                <div class="profile-field">
                    <span class="profile-label">Email:</span>
                    <span class="profile-value">${currentUser.email}</span>
                </div>
                <div class="profile-field">
                    <span class="profile-label">Phone:</span>
                    <span class="profile-value">${currentUser.phone}</span>
                </div>
                <div class="profile-field">
                    <span class="profile-label">Age:</span>
                    <span class="profile-value">${currentUser.age}</span>
                </div>
                <div class="profile-field">
                    <span class="profile-label">Gender:</span>
                    <span class="profile-value">${currentUser.gender === 'M' ? 'Male' : currentUser.gender === 'F' ? 'Female' : 'Other'}</span>
                </div>
                <div class="profile-field">
                    <span class="profile-label">Address:</span>
                    <span class="profile-value">${currentUser.address}</span>
                </div>
            </div>
        `;
        profileInfo.innerHTML = html;
    } catch (error) {
        showToast('Failed to load profile: ' + error.message, 'error');
    }
}

// Admin Functions
async function handleAdminLogin(event) {
    event.preventDefault();

    const username = document.getElementById('adminUsername').value;
    const password = document.getElementById('adminPassword').value;

    // Simple demo authentication (replace with real backend)
    if (username === 'admin' && password === 'admin123') {
        currentUser = { adminId: 1, name: 'Administrator' };
        currentUserType = 'admin';

        showToast('Admin login successful!', 'success');
        updateNavbar();
        showAdminDashboard();
    } else {
        showToast('Invalid credentials', 'error');
    }
}

async function loadPendingRequests() {
    try {
        const requests = API.getPendingRequests();
        let html = '';

        if (requests.length > 0) {
            requests.forEach(req => {
                const patient = API.patients.find(p => p.patientId === req.patientId);
                html += `
                    <div class="request-card">
                        <div class="card-header">
                            <div class="card-title">${patient ? patient.name : 'Unknown Patient'}</div>
                            <span class="card-badge badge-pending">PENDING</span>
                        </div>
                        <div class="card-info">
                            <div class="info-item">
                                <span class="info-label">Specialization</span>
                                <span class="info-value">${req.specialization}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Requested Date</span>
                                <span class="info-value">${new Date(req.requestedDate).toLocaleDateString()}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Patient Email</span>
                                <span class="info-value">${patient ? patient.email : 'N/A'}</span>
                            </div>
                        </div>
                        ${req.description ? `<p><strong>Description:</strong> ${req.description}</p>` : ''}
                        <div class="card-actions">
                            <button class="btn btn-primary btn-sm" onclick="fixAppointment(${req.requestId}, ${req.patientId}, '${req.specialization}', '${req.requestedDate}')">Assign Doctor</button>
                        </div>
                    </div>
                `;
            });
        } else {
            html = '<p class="empty-state">No pending requests</p>';
        }

        document.getElementById('pendingRequestsList').innerHTML = html;
    } catch (error) {
        showToast('Failed to load pending requests: ' + error.message, 'error');
    }
}

async function loadDoctorList() {
    try {
        const doctors = API.getDoctors();
        let html = '';

        if (doctors.length > 0) {
            doctors.forEach(doc => {
                html += `
                    <div class="doctor-card">
                        <div class="card-header">
                            <div class="card-title">${doc.name}</div>
                            <span class="card-badge" style="background-color: ${doc.available ? '#d1fae5' : '#fee2e2'}; color: ${doc.available ? '#065f46' : '#991b1b'};">
                                ${doc.available ? 'Available' : 'Unavailable'}
                            </span>
                        </div>
                        <div class="card-info">
                            <div class="info-item">
                                <span class="info-label">Specialization</span>
                                <span class="info-value">${doc.specialization}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Experience</span>
                                <span class="info-value">${doc.experience} years</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Email</span>
                                <span class="info-value">${doc.email}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Phone</span>
                                <span class="info-value">${doc.phone}</span>
                            </div>
                        </div>
                    </div>
                `;
            });
        } else {
            html = '<p class="empty-state">No doctors available</p>';
        }

        document.getElementById('doctorsList').innerHTML = html;
    } catch (error) {
        showToast('Failed to load doctors: ' + error.message, 'error');
    }
}

async function handleAddDoctor(event) {
    event.preventDefault();

    const data = {
        name: document.getElementById('doctorName').value,
        specialization: document.getElementById('doctorSpecialization').value,
        email: document.getElementById('doctorEmail').value,
        phone: document.getElementById('doctorPhone').value,
        qualification: document.getElementById('doctorQualification').value,
        experience: parseInt(document.getElementById('doctorExperience').value)
    };

    try {
        const doctor = API.addDoctor(data);
        showToast('Doctor added successfully!', 'success');
        document.getElementById('addDoctorForm').reset();
        setTimeout(() => showDoctorList(), 1500);
    } catch (error) {
        showToast('Failed to add doctor: ' + error.message, 'error');
    }
}

async function loadAllAppointments() {
    try {
        const appointments = API.getAllAppointments();
        let html = '';

        if (appointments.length > 0) {
            appointments.forEach(appt => {
                const patient = API.patients.find(p => p.patientId === appt.patientId);
                const doctor = API.doctors.find(d => d.doctorId === appt.doctorId);
                html += `
                    <div class="appointment-card">
                        <div class="card-header">
                            <div class="card-title">${patient ? patient.name : 'Unknown'} - ${doctor ? doctor.name : 'Unknown'}</div>
                            <span class="card-badge badge-confirmed">${appt.status}</span>
                        </div>
                        <div class="card-info">
                            <div class="info-item">
                                <span class="info-label">Patient</span>
                                <span class="info-value">${patient ? patient.email : 'N/A'}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Doctor</span>
                                <span class="info-value">${doctor ? doctor.specialization : 'N/A'}</span>
                            </div>
                            <div class="info-item">
                                <span class="info-label">Date & Time</span>
                                <span class="info-value">${new Date(appt.appointmentDate).toLocaleString()}</span>
                            </div>
                        </div>
                    </div>
                `;
            });
        } else {
            html = '<p class="empty-state">No appointments</p>';
        }

        document.getElementById('allAppointmentsList').innerHTML = html;
    } catch (error) {
        showToast('Failed to load appointments: ' + error.message, 'error');
    }
}

function fixAppointment(requestId, patientId, specialization, requestedDate) {
    const availableDoctors = API.getDoctorsBySpecialization(specialization);

    if (availableDoctors.length === 0) {
        showToast('No available doctors for this specialization', 'warning');
        return;
    }

    const doctor = availableDoctors[0];

    const data = {
        requestId: requestId,
        patientId: patientId,
        doctorId: doctor.doctorId,
        appointmentDate: requestedDate
    };

    try {
        const appointment = API.fixAppointment(data);
        showToast(`Appointment confirmed with ${doctor.name}!`, 'success');
        setTimeout(() => loadPendingRequests(), 1500);
    } catch (error) {
        showToast('Failed to fix appointment: ' + error.message, 'error');
    }
}

// Utility Functions
function showToast(message, type = 'info') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast show ${type}`;

    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

function updateNavbar() {
    const logoutLink = document.getElementById('logoutLink');
    if (currentUser) {
        logoutLink.style.display = 'block';
    } else {
        logoutLink.style.display = 'none';
    }
}

function logout() {
    currentUser = null;
    currentUserType = null;
    showToast('Logged out successfully', 'success');
    updateNavbar();
    showHome();
}

// Set minimum date to today
function setMinDate() {
    const dateInput = document.getElementById('appointmentDate');
    const today = new Date().toISOString().split('T')[0];
    dateInput.setAttribute('min', today);
}

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    setMinDate();
    updateNavbar();
});
