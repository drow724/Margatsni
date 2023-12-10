import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import "./App.css";
import MapRoute from "./routes/MapRoute";
import "mapbox-gl/dist/mapbox-gl.css";
import LoginRoute from "./routes/LoginRoute";

function App() {
  return (
    <Router>
      <Routes basename={process.env.PUBLIC_URL}>
        <Route path={process.env.PUBLIC_URL} element={<MapRoute />} />
        <Route
          path={`${process.env.PUBLIC_URL}/login`}
          element={<LoginRoute />}
        />
      </Routes>
    </Router>
  );
}

export default App;
