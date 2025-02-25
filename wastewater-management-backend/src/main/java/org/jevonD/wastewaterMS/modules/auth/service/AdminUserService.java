package org.jevonD.wastewaterMS.modules.auth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jevonD.wastewaterMS.modules.auth.dto.admin.*;
import org.jevonD.wastewaterMS.modules.auth.entity.SysUser;
import java.util.List;

public interface AdminUserService {

    /**
     * Creates a new user with the provided details.
     * @param req The request containing user creation details.
     * @return The created SysUser entity.
     */
    SysUser createUser(AdminCreateReq req);

    /**
     * Deletes a user by their ID.
     * @param userId The ID of the user to delete.
     * @return The response detailing the outcome of the deletion.
     */
    AdminDeleteResp deleteUser(Long userId);

    /**
     * Resets the password for a user by their ID.
     * @param userId The ID of the user whose password is to be reset.
     * @return The response with the reset password details.
     */
    AdminResetPasswordResp resetUserPassword(Long userId);

    /**
     * Lists all users based on pagination and query criteria.
     * @param page The page number to retrieve.
     * @param size The number of records per page.
     * @param queryReq The filtering criteria for querying users.
     * @return The paged response of users.
     */
    AdminQueryResp listUsers(int page, int size, AdminQueryReq queryReq);

    /**
     * Updates a user's information based on the provided request details.
     * @param req The request containing the details for the update.
     * @return The response with the updated user details.
     */
    AdminUpdateResp updateUser(AdminUpdateReq req);

    /**
     * Updates the roles assigned to a user.
     * @param userId The ID of the user whose roles are to be updated.
     * @param roleCode The new role code to assign to the user.
     */
    void updateUserRoles(Long userId, String roleCode);

    /**
     * Updates the status of a user (enabled or disabled).
     * @param userId The ID of the user whose status is to be updated.
     * @param enabled The new status to set (true for enabled, false for disabled).
     */
    void updateUserStatus(Long userId, boolean enabled);
}
