# Hospital Management System - Frontend

A modern, responsive web-based frontend for the Hospital Management System, built with HTML5, CSS3, and vanilla JavaScript.

## Features

### Patient Portal
- **User Registration**: Register with personal details
- **Login**: Email-based authentication
- **Request Appointments**: Request appointments by selecting specialization and date
- **View Appointments**: See pending requests and confirmed appointments
- **Profile Management**: View and manage personal profile

### Admin Portal
- **Pending Requests**: View and manage pending appointment requests
- **Doctor Management**: Add new doctors and view doctor list
- **Appointment Assignment**: Assign available doctors to appointment requests
- **View All Appointments**: See all confirmed appointments in the system

## Technology Stack

- **HTML5**: Semantic markup
- **CSS3**: Responsive design with Flexbox and CSS Grid
- **JavaScript (ES6+)**: Vanilla JS (no frameworks)
- **Font Awesome Icons**: For UI icons
- **Local Storage**: For session management (optional enhancement)

## Project Structure

```
frontend/
├── index.html                    # Main HTML file
├── css/
│   └── styles.css               # Complete styling
├── js/
│   ├── api.js                   # API client and mock data
│   └── app.js                   # Main application logic
└── README.md                    # This file
```

## Getting Started

### Prerequisites
- Any modern web browser (Chrome, Firefox, Safari, Edge)
- No build tools or dependencies required
- Optional: Backend server running on `http://localhost:8080`

### Installation

1. **Clone or download the frontend files**:
   ```bash
   git clone https://github.com/Shoaib-ahamad/Hospital-Management-System.git
   cd Hospital-Management-System/frontend
   ```

2. **Open in browser**:
   ```bash
   # On Windows
   start index.html
   
   # On macOS
   open index.html
   
   # Or simply double-click index.html
   ```

3. **Access the application**:
   - Open `http://localhost:5500/index.html` (if using Live Server)
   - Or `file:///path/to/frontend/index.html` (local file)

### Using with Backend

To connect to the real backend API:

1. **Update API Base URL** in `js/api.js`:
   ```javascript
   const API_BASE_URL = 'http://localhost:8080/api'; // Change port if needed
   ```

2. **Switch from Mock to Real API** in `js/api.js`:
   ```javascript
   // Change this line:
   const API = MockDataService;
   
   // To:
   const API = APIClient;
   ```

3. **Ensure backend is running** on the specified URL

## Demo Credentials

### Patient Portal
- **Email**: john.doe@email.com (pre-loaded)
- **Note**: Any email can be used to register new patients

### Admin Portal
- **Username**: `admin`
- **Password**: `admin123`

## Features Detailed

### Patient Features

#### 1. Registration
- Full name, email, phone, age, gender, address
- Email uniqueness validation
- Automatic patient ID assignment

#### 2. Login
- Simple email-based authentication
- Session management

#### 3. Request Appointment
- Select specialization (Cardiology, Neurology, General, Pediatrics)
- Choose preferred date
- Optional description of symptoms
- Real-time request tracking

#### 4. View Appointments
- Pending appointment requests with status
- Confirmed appointments with doctor details
- Appointment date and time information

#### 5. Profile Management
- View personal information
- Display all profile fields

### Admin Features

#### 1. Pending Requests
- View all pending appointment requests
- Patient details (name, email, contact)
- Specialization and date requirements
- Quick appointment assignment button

#### 2. Doctor Management
- Add new doctors with details
- View all doctors with availability status
- Specialization and experience display
- Edit doctor availability

#### 3. Appointment Assignment
- Assign available doctors to requests
- Automatic doctor selection by specialization
- Appointment confirmation
- Request status update

#### 4. View All Appointments
- Complete list of confirmed appointments
- Patient-doctor pairing information
- Appointment date and status

## UI/UX Design

### Design Principles
- **Responsive**: Works on desktop, tablet, and mobile
- **Modern**: Clean, gradient-based design
- **Accessible**: ARIA labels and semantic HTML
- **User-Friendly**: Intuitive navigation and clear calls-to-action

### Color Scheme
- **Primary**: Blue (#2563eb)
- **Secondary**: Purple (#7c3aed)
- **Success**: Green (#10b981)
- **Danger**: Red (#ef4444)
- **Light**: Light Gray (#f3f4f6)
- **Dark**: Dark Gray (#1f2937)

### Components
- Navigation bar with brand and menu
- Hero section on homepage
- Feature cards
- Form inputs with validation
- Dashboard with sidebar navigation
- Card-based list layouts
- Toast notifications
- Responsive grid layouts

## API Integration

### Mock Service (Default)
The frontend comes with a mock service that simulates backend responses. This allows testing without a running backend server.

**Usage**: Automatically active in `js/api.js`

### Real API Client
Ready for production backend integration.

**Methods**:
- `APIClient.registerPatient(data)` - Register new patient
- `APIClient.loginPatient(email)` - Patient login
- `APIClient.requestAppointment(data)` - Request appointment
- `APIClient.addDoctor(data)` - Add new doctor
- `APIClient.fixAppointment(data)` - Assign doctor to request
- `APIClient.getPendingRequests()` - Get pending requests
- And more...

## Responsive Design

### Breakpoints
- **Desktop**: 1024px and above
- **Tablet**: 768px to 1023px
- **Mobile**: Below 768px
- **Small Mobile**: Below 480px

### Features
- Hamburger menu on mobile
- Single-column layout on mobile
- Flexible grid layouts
- Readable font sizes on all devices
- Touch-friendly button sizes

## Browser Compatibility

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)
- Mobile browsers (iOS Safari, Chrome Mobile)

## Customization

### Changing Colors
Edit CSS variables in `css/styles.css`:
```css
:root {
    --primary-color: #2563eb;
    --secondary-color: #7c3aed;
    /* ... other colors ... */
}
```

### Adding New Features
1. Add HTML structure in `index.html`
2. Add styling in `css/styles.css`
3. Add functionality in `js/app.js`
4. Update API calls in `js/api.js`

### Modifying Forms
- Edit form fields in `index.html`
- Update validation in `js/app.js`
- Update API calls with new fields

## Performance Considerations

- Lightweight CSS with no external dependencies
- Vanilla JavaScript (no framework overhead)
- Efficient DOM manipulation
- Local mock data for faster testing
- Responsive images and assets
- Minimal HTTP requests

## Future Enhancements

1. **Local Storage**: Persist user sessions
2. **Offline Mode**: Service workers for offline functionality
3. **Dark Mode**: Theme switching
4. **Real-time Updates**: WebSocket integration
5. **File Uploads**: Medical reports/documents
6. **Notifications**: Desktop/browser notifications
7. **Analytics**: Usage tracking
8. **Internationalization**: Multi-language support
9. **Accessibility**: Enhanced WCAG compliance
10. **PWA**: Progressive web app features

## Troubleshooting

### Issue: Blank page or styling not loading
**Solution**: 
- Check file paths in `index.html`
- Ensure CSS and JS files are in correct directories
- Clear browser cache

### Issue: API calls not working
**Solution**:
- Check if backend server is running
- Verify API base URL in `js/api.js`
- Check browser console for errors
- Ensure CORS is enabled on backend

### Issue: Forms not submitting
**Solution**:
- Check browser console for JavaScript errors
- Verify form field IDs match JavaScript code
- Check validation requirements
- Enable mock service if backend unavailable

## Development

### Local Server
For better development experience, use a local server:

**Option 1: Python**
```bash
# Python 3
python -m http.server 5500

# Python 2
python -m SimpleHTTPServer 5500
```

**Option 2: Node.js (http-server)**
```bash
npm install -g http-server
http-server -p 5500
```

**Option 3: VS Code Live Server**
- Install Live Server extension
- Right-click on `index.html`
- Select "Open with Live Server"

### Testing
- Test on multiple devices and browsers
- Test responsive design with browser DevTools
- Verify form validation
- Test with and without backend connection

## Security Considerations

**Note**: This is a frontend only. For production:

1. **Backend Validation**: Always validate on server
2. **Authentication**: Implement proper JWT/session management
3. **HTTPS**: Use SSL/TLS in production
4. **CORS**: Configure properly on backend
5. **Input Sanitization**: Sanitize user inputs
6. **Rate Limiting**: Implement rate limiting
7. **Data Encryption**: Encrypt sensitive data

## Support & Contributions

For issues, suggestions, or contributions:
- GitHub: https://github.com/Shoaib-ahamad/Hospital-Management-System
- Create an issue or pull request

## License

This project is open-source and available under the MIT License.

## Credits

Built as part of the Hospital Management System project demonstrating three-tier architecture and web development best practices.

---

**Version**: 1.0.0  
**Last Updated**: November 15, 2025  
**Status**: Production Ready
