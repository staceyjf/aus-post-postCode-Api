import { useState, useContext } from "react";
import LoginForm from "../../components/LoginForm/LoginForm";
import { UserContext } from "../../context/userContextProvider";
import { Backdrop, Snackbar } from "@mui/material";
import Alert from "@mui/material/Alert";
import styles from "./LoginContainer.module.scss";

const LoginContainer = () => {
  const [error, setError] = useState<Error | null>(null);
  const { user, userSignIn, signOut } = useContext(UserContext);

  const onSubmit = (username: string, password: string) => {
    userSignIn(username, password).catch((e: any) => {
      setError(new Error("Failed to sign in. Please try again."));
      console.error("ERROR: " + e);
    });
  };

  console.log(user);

  return (
    <>
      {error && (
        <Backdrop open={true} sx={{ color: "#fff", zIndex: 1 }}>
          <Snackbar
            open={true}
            autoHideDuration={6000}
            onClose={() => setError(null)}
          >
            <Alert
              severity="error"
              variant="filled"
              sx={{ width: "100%" }}
              aria-live="assertive"
              data-testid="error-alert"
            >
              {error?.message}
            </Alert>
          </Snackbar>
        </Backdrop>
      )}
      {!error && !user && (
        <LoginForm
          placeholderUsername="Username"
          placeholderPassword="Password"
          onSubmit={onSubmit}
        />
      )}
      {!error && user && (
        <button onClick={signOut} className={styles.auth_LogoutBtn}>
          Logout
        </button>
      )}
    </>
  );
};

export default LoginContainer;
