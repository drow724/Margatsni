import axios from "axios";
import React, { useMemo, useRef, useState } from "react";
import { Marker } from "react-map-gl";
import Pin from "../components/Pin";
import bbox from "@turf/bbox";
import Map from "../components/Map";
import { Box } from "@mui/system";
import Switch from "@mui/material/Switch";
import { CircularProgress } from "@mui/material";
import RefreshIcon from "@mui/icons-material/Refresh";
import IconButton from "@mui/material/IconButton";

const label = { inputProps: { "aria-label": "Switch demo" } };

const handleClick = () => {
  console.log("click");
};

function MapHook({ accessToken, updating }) {
  const [loading, setLoading] = useState(true);

  const [contents, setContents] = useState([]);

  const [popupInfo, setPopupInfo] = useState(null);

  const mapRef = useRef();

  const onClick = (event) => {
    const feature = event.features[0];
    if (feature) {
      // calculate the bounding box of the feature
      const [minLng, minLat, maxLng, maxLat] = bbox(feature);
      mapRef.current.fitBounds(
        [
          [minLng, minLat],
          [maxLng, maxLat],
        ],
        { padding: 40, duration: 1000 }
      );
    }
  };

  const [bounds, setBounds] = useState([]);

  const setting = async () => {
    await axios({
      method: "get",
      url: `http://localhost:7060?accessToken=${accessToken}`,
    }).then((response) => {
      setContents(response.data);
      setLoading(false);

      const latList = response.data.map((d) => d.location.lng);

      const minLat = latList.reduce((acc, cur) =>
        cur.location.lat > acc.location.lat
          ? acc.location.lat
          : cur.location.lat
      );

      const maxLat = latList.reduce((acc, cur) =>
        cur.location.lat < acc.location.lat
          ? acc.location.lat
          : cur.location.lat
      );

      const lngList = response.data.map((d) => d.location.lng);

      const minLng = lngList.reduce((acc, cur) =>
        cur.location.lng > acc.location.lng
          ? acc.location.lng
          : cur.location.lng
      );

      const maxLng = lngList.reduce((acc, cur) =>
        cur.location.lng < acc.location.lng
          ? acc.location.lng
          : cur.location.lng
      );

      console.log(minLat, minLng, maxLat, maxLng);
      setBounds([
        [minLng, minLat],
        [maxLng, maxLat],
      ]);
    });
  };

  useState(() => {
    setting();
  }, []);

  const pins = useMemo(
    () =>
      contents.map((content, index) => (
        <Marker
          key={`marker-${index}`}
          longitude={content.location.lng}
          latitude={content.location.lat}
          anchor="bottom"
          onClick={(e) => {
            // If we let the click event propagates to the map, it will immediately close the popup
            // with `closeOnClick: true`
            e.originalEvent.stopPropagation();
            const target = {
              center: [e.target._lngLat.lng, e.target._lngLat.lat],
              //zoom: 5,
            };
            mapRef.current.flyTo({
              ...target, // Fly to the selected target
              duration: 2000, // Animate over 12 seconds
              essential: true, // This animation is considered essential with
              //respect to prefers-reduced-motion
            });
            setPopupInfo(content);
          }}
        >
          <Pin />
        </Marker>
      )),
    [contents]
  );

  const bottom = (
    <div
      style={{
        position: "fixed",
        width: "20%",
        height: "100%",
        margin: "0 auto",
        left: 0,
        right: 0,
        bottom: 0,
        top: 0,
        display: "flex",
        justifyContent: "center",
        flexDirection: "column",
        alignItems: "center",
        transform: "translate(230%, -45%)",
      }}
    >
      <IconButton aria-label="delete" onClick={handleClick}>
        <RefreshIcon
        //checked={checked}
        // {...label}
        />
      </IconButton>

      {updating && (
        <Box sx={{ display: "flex" }}>
          <CircularProgress color="secondary" />
        </Box>
      )}
    </div>
  );
  return (
    <Map
      loading={loading}
      bounds={bounds}
      onClick={onClick}
      pins={pins}
      popupInfo={popupInfo}
      setPopupInfo={setPopupInfo}
      bottom={bottom}
      ref={mapRef}
    />
  );
}

export default MapHook;
