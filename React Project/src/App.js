import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import "./App.css";
import MapRoute from "./routes/MapRoute";
import "mapbox-gl/dist/mapbox-gl.css";
import LoginRoute from "./routes/LoginRoute";

function App() {
  return (
    <Router basename={process.env.PUBLIC_URL}>
      <Routes>
        <Route exact path="/" element={<MapRoute />} />
        <Route exact path="/login" element={<LoginRoute />} />
      </Routes>
    </Router>
  );
}

export default App;
