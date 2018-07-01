package ai.portfolio.dev.project.app.com.padoi.Interfaces;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public interface IPadoiAuth {
  /**
   * Async approach to makking a http request to firebase
   * @param user from frirebase Auth api
   * @return a loader callback to tell the program how to respond after request
   */
  DatabaseReference fetchPadoiUser(FirebaseUser user);

    /**
     * Async add to firebase/users/{id}
     * @param user fb api user
     * @return
     */
  DatabaseReference addNewPadoiUser(FirebaseUser user);
}
