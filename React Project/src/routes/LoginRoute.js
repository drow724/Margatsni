import React, { useEffect } from "react";

export default function SignIn() {
  useEffect(() => {
    const currentUrl = window.location.href;
    const searchParams = new URL(currentUrl).searchParams;
    const code = searchParams.get("code");
    if (code) {
      window.opener.postMessage({ code }, window.location.origin);
    }
  }, []);
  return <div>login.....</div>;
}
