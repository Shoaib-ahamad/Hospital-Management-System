// API Configuration
const API_BASE_URL = 'http://localhost:8080/api'; // Change this to your backend URL

// API Client
class APIClient {
    // Patient APIs
    static async registerPatient(data) {
        return this._post('/patients/register', data);
    }

    static async loginPatient(email) {
        return this._get(`/patients/login?email=${encodeURIComponent(email)}`);
    }

    static async getPatientById(id) {
        return this._get(`/patients/${id}`);
    }

    static async getAllPatients() {
        return this._get('/patients');
    }

    // Doctor APIs
    static async addDoctor(data) {
        return this._post('/doctors', data);
    }

    static async getAllDoctors() {
        return this._get('/doctors');
    }

    static async getDoctorsBySpecialization(specialization) {
        return this._get(`/doctors/specialization/${encodeURIComponent(specialization)}`);
    }

    static async getDoctorById(id) {
        return this._get(`/doctors/${id}`);
    }

    // Appointment APIs
    static async requestAppointment(data) {
        return this._post('/appointments/request', data);
    }

    static async getAppointmentsByPatientId(patientId) {
        return this._get(`/appointments/patient/${patientId}`);
    }

    static async getAllAppointments() {
        return this._get('/appointments');
    }

    static async fixAppointment(data) {
        return this._post('/appointments/fix', data);
    }

    // Appointment Request APIs
    static async getPendingRequests() {
        return this._get('/appointment-requests/pending');
    }

    static async getAppointmentRequestsByPatientId(patientId) {
        return this._get(`/appointment-requests/patient/${patientId}`);
    }

    // Helper methods
    static async _get(endpoint) {
        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            return await this._handleResponse(response);
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    }

    static async _post(endpoint, data) {
        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });
            return await this._handleResponse(response);
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    }

    static async _handleResponse(response) {
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'API Error');
        }
        return await response.json();
    }
}

// Mock Data for Demo (when backend is not available)
class MockDataService {
    static patients = [
        { patientId: 1, name: 'John Doe', email: 'john.doe@email.com', phone: '1234567890', age: 35, gender: 'M', address: '123 Main St' },
        { patientId: 2, name: 'Jane Smith', email: 'jane.smith@email.com', phone: '0987654321', age: 28, gender: 'F', address: '456 Oak Ave' }
    ];

    static doctors = [
        { doctorId: 1, name: 'Dr. Sarah Williams', specialization: 'Cardiology', email: 'sarah@hospital.com', phone: '1111111111', qualification: 'MD', experience: 10, available: true },
        { doctorId: 2, name: 'Dr. Michael Brown', specialization: 'Neurology', email: 'michael@hospital.com', phone: '2222222222', qualification: 'MD', experience: 15, available: true },
        { doctorId: 3, name: 'Dr. Emily Davis', specialization: 'General', email: 'emily@hospital.com', phone: '3333333333', qualification: 'MD', experience: 8, available: true }
    ];

    static appointments = [];

    static appointmentRequests = [];

    static registerPatient(data) {
        const newPatient = {
            patientId: this.patients.length + 1,
            ...data
        };
        this.patients.push(newPatient);
        return newPatient;
    }

    static loginPatient(email) {
        const patient = this.patients.find(p => p.email === email);
        if (!patient) {
            throw new Error('Patient not found');
        }
        return patient;
    }

    static getPatientById(id) {
        return this.patients.find(p => p.patientId === parseInt(id));
    }

    static addDoctor(data) {
        const newDoctor = {
            doctorId: this.doctors.length + 1,
            available: true,
            ...data
        };
        this.doctors.push(newDoctor);
        return newDoctor;
    }

    static getDoctors() {
        return this.doctors;
    }

    static getDoctorsBySpecialization(spec) {
        return this.doctors.filter(d => d.specialization === spec && d.available);
    }

    static requestAppointment(data) {
        const newRequest = {
            requestId: this.appointmentRequests.length + 1,
            status: 'PENDING',
            createdAt: new Date().toISOString(),
            ...data
        };
        this.appointmentRequests.push(newRequest);
        return newRequest;
    }

    static getPendingRequests() {
        return this.appointmentRequests.filter(r => r.status === 'PENDING');
    }

    static getAppointmentsByPatientId(patientId) {
        return this.appointments.filter(a => a.patientId === parseInt(patientId));
    }

    static getAppointmentRequests(patientId) {
        return this.appointmentRequests.filter(r => r.patientId === parseInt(patientId));
    }

    static getAllAppointments() {
        return this.appointments;
    }

    static fixAppointment(data) {
        const newAppointment = {
            appointmentId: this.appointments.length + 1,
            status: 'CONFIRMED',
            createdAt: new Date().toISOString(),
            ...data
        };
        this.appointments.push(newAppointment);

        // Update request status
        const request = this.appointmentRequests.find(r => r.requestId === data.requestId);
        if (request) {
            request.status = 'APPROVED';
        }

        return newAppointment;
    }
}

// Use Mock Service by default (replace with APIClient when backend is ready)
const API = MockDataService;
