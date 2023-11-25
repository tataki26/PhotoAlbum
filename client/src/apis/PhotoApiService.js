import { apiClient } from "./ApiClient";

export const movePhotoApi
    = (id, photo) => apiClient.put(`/albums/${id}/photos/move`, photo);

export const deletePhotoApi
    = (id, photoIds) => apiClient.delete(`/albums/${id}/photos`, {
        data: {photoIds}
    });