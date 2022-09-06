import { BrowserRouter, Route, Routes } from 'react-router-dom';
import SignInPage from './pages/SignInPage';
import HomePage from './pages/HomePage';
import 'antd/dist/antd.css';
import './styles/main.css';
import './styles/responsive.css';

const App = () => (
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<SignInPage />} />
      <Route path="home" element={<HomePage />} />
    </Routes>
  </BrowserRouter>
);

export default App;
