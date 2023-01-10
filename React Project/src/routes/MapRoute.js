import axios from "axios";
import React, { useEffect, useMemo, useRef, useState } from "react";
import { ProgressBar } from "react-loader-spinner";
import { useParams } from "react-router-dom";
import {
  clusterLayer,
  clusterCountLayer,
  unclusteredPointLayer,
} from "../components/layers";
import { Map, Source, Layer, Marker, Popup } from "react-map-gl";
import Pin from "../components/Pin";
import SwipeableTextMobileStepper from "../components/SwipeableTextMobileStepper";
import { maxParallelImageRequests } from "mapbox-gl";
import bbox from "@turf/bbox";
import styled from "styled-components";
import "./MapRoute.css";
import { CircularProgress } from "@mui/material";
import { Box } from "@mui/system";

const MAPBOX_TOKEN =
  "pk.eyJ1IjoiZHJvdzcyNCIsImEiOiJjbGI3dGpiZ3AwZGRvM3NvMnU5a2w3ZHh4In0.Ei81FJmfrOdiB2Rn2rlKyA";

function MapRoute() {
  const params = useParams();

  const [loading, setLoading] = useState(true);

  const [contents, setContents] = useState([]);

  const [lng, setLng] = useState(-70.9);
  const [lat, setLat] = useState(42.35);
  const [zoom, setZoom] = useState(2);

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

  const setting = async () => {
    await axios({
      method: "get",
      url: `http://146.56.38.5:8080/${params.username}`,
    }).then((response) => {
      setContents(response.data);
      setLng(() => {
        return (
          response.data.reduce((acc, cur) => {
            return (acc += cur.location.lng);
          }, 0) / response.data.length
        );
      });
      setLat(
        response.data.reduce((acc, cur) => {
          return (acc += cur.location.lat);
        }, 0) / response.data.length
      );
      setLoading(false);
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

  return loading ? (
    <React.Fragment>
      <Box
        sx={{
          display: "flex",
          alignItems: "center",
          height: "100vh",
          width: "100%",
          justifyContent: "center",
          backgroundColor: "#292929",
        }}
      >
        <CircularProgress size={100} disableShrink />
      </Box>
    </React.Fragment>
  ) : (
    <React.Fragment>
      <Map
        initialViewState={{
          longitude: lng,
          latitude: lat,
          zoom: zoom,
        }}
        mapboxAccessToken={MAPBOX_TOKEN}
        style={{ width: "100%", height: "100vh" }}
        mapStyle="mapbox://styles/mapbox/dark-v11"
        interactiveLayerIds={[clusterLayer.id]}
        ref={mapRef}
        onClick={onClick}
      >
        {pins}

        {popupInfo && (
          <React.Fragment>
            <Popup
              anchor="top"
              longitude={Number(popupInfo.location.lng)}
              latitude={Number(popupInfo.location.lat)}
              onClose={() => setPopupInfo(null)}
              style={{
                width: "100%",
                borderRadius: 10,
                alignItems: "center",
                justifyContent: "space-between",
              }}
            >
              <SwipeableTextMobileStepper info={popupInfo} />
            </Popup>
          </React.Fragment>
        )}
      </Map>
    </React.Fragment>
  );
}

export default MapRoute;
