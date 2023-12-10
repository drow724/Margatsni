import { Face } from "@mui/icons-material";
import React, { useRef, useState, useMemo, useEffect } from "react";
import { Map, Layer } from "react-map-gl";
import {
  FacebookLoginButton,
  InstagramLoginButton,
  TwitterLoginButton,
} from "react-social-login-buttons";

import axios from "axios";

import { Form, useNavigate } from "react-router-dom";

const MAPBOX_TOKEN =
  "pk.eyJ1IjoiZHJvdzcyNCIsImEiOiJjbGI3dGpiZ3AwZGRvM3NvMnU5a2w3ZHh4In0.Ei81FJmfrOdiB2Rn2rlKyA";

function MapHook() {
  const navigate = useNavigate();

  const mapRef = useRef();

  const [popup, setPopup] = useState(null);
  const [code, setCode] = useState(null);

  useEffect(() => {
    if (!popup) {
      return;
    }

    const githubOAuthCodeListener = (e) => {
      // 동일한 Origin 의 이벤트만 처리하도록 제한
      if (e.origin !== window.location.origin) {
        return;
      }
      const { code } = e.data;
      console.log(e);
      if (code) {
        console.log(`The popup URL has URL code param = ${code}`);
        setCode(code);
      }
      popup?.close();
      setPopup(null);
    };

    window.addEventListener("message", githubOAuthCodeListener, false);

    return () => {
      window.removeEventListener("message", githubOAuthCodeListener);
      popup?.close();
      setPopup(null);
    };
  }, [popup]);

  useEffect(() => {
    if (!code) {
      return;
    }

    axios
      .get(`https://localhost:7060/login/accessToken?code=${code}`)
      .then((response) => {
        console.log(response);
      });
  }, [code]);

  return (
    <React.Fragment>
      <Map
        ref={mapRef}
        mapboxAccessToken={MAPBOX_TOKEN}
        style={{ width: "100%", height: "100vh", position: "fixed" }}
        mapStyle="mapbox://styles/mapbox/dark-v11"
      ></Map>
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
        }}
      >
        <InstagramLoginButton
          onClick={() => {
            const popup = window.open(
              "https://api.instagram.com/oauth/authorize?client_id=1202988193978178&redirect_uri=https://localhost:3000/margatsni/login&response_type=code&scope=user_profile,user_media",
              "인스타그램 로그인",
              "popup=yes"
            );
            setPopup(popup);
          }}
        />
      </div>
    </React.Fragment>
  );
}

export default MapHook;
