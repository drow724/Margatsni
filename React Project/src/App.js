import { HashRouter as Router, Route, Routes } from "react-router-dom";
import "./App.css";
import MapRoute from "./routes/MapRoute";
import "mapbox-gl/dist/mapbox-gl.css";

function App() {
  return (
    <Router>
      <Routes basename={process.env.PUBLIC_URL}>
        <Route path="/" element={<MapRoute />} />
      </Routes>
    </Router>
  );
}

export default App;
