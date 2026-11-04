import { HashRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/layout/Navbar";
import Footer from "./components/layout/Footer";
import MobileMenu from "./components/layout/MobileMenu";
import Home from "./pages/Home";
import About from "./pages/About";
import Programs from "./pages/Programs";
import Equipment from "./pages/Equipment";
import Trainers from "./pages/Trainers";
import Membership from "./pages/Membership";
import Gallery from "./pages/Gallery";
import FAQ from "./pages/FAQ";
import Contact from "./pages/Contact";
import BookTrial from "./pages/BookTrial";
import BookAppointment from "./pages/BookAppointment";
import Join from "./pages/Join";
import AdminDashboard from "./pages/AdminDashboard";
import LoginPage from "./pages/Login";
import RegisterPage from "./pages/Register";
import AdminProgramNew from "./pages/AdminProgramNew";
import AdminEquipmentNew from "./pages/AdminEquipmentNew";
import AdminTrainerNew from "./pages/AdminTrainerNew";
import AdminGalleryNew from "./pages/AdminGalleryNew";
import "./App.css";

function App() {
  return (
    <HashRouter>
      <Navbar />
      <MobileMenu />
      <main style={{ minHeight: "100vh" }}>
        <Routes>
          {/* PUBLIC ROUTES */}
          <Route path="/" element={<Home />} />
          <Route path="/about" element={<About />} />
          <Route path="/programs" element={<Programs />} />
          <Route path="/equipment" element={<Equipment />} />
          <Route path="/trainers" element={<Trainers />} />
          <Route path="/membership" element={<Membership />} />
          <Route path="/gallery" element={<Gallery />} />
          <Route path="/faq" element={<FAQ />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/book-trial" element={<BookTrial />} />
          <Route path="/book-appointment" element={<BookAppointment />} />
          <Route path="/join" element={<Join />} />

          {/* AUTHENTICATION ROUTES */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          {/* ADMIN ROUTES */}
          <Route path="/admin/dashboard" element={<AdminDashboard />} />
          <Route path="/admin/programs/new" element={<AdminProgramNew />} />
          <Route path="/admin/equipment/new" element={<AdminEquipmentNew />} />
          <Route path="/admin/trainers/new" element={<AdminTrainerNew />} />
          <Route path="/admin/gallery/new" element={<AdminGalleryNew />} />
        </Routes>
      </main>
      <Footer />
    </HashRouter>
  );
}

export default App;
