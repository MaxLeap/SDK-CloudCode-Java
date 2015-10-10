package com.maxleap.code.impl;

import com.maxleap.las.sdk.*;
import com.maxleap.code.*;

/**
 * Handler for entity request.
 */
public final class LASClassManagerHandler<T> implements LASHandler<LASClassManagerRequest, Response> {

  private static final Logger logger = LoggerFactory.getLogger(LASClassManagerHandler.class);

  private final LASClassManager<T> entityManager;
  private final Class<T> entityClazz;

  public LASClassManagerHandler(LASClassManager<T> entityManager, Class<T> entityClazz) {
    this.entityManager = entityManager;
    this.entityClazz = entityClazz;
  }

  @Override
  public Response handle(LASClassManagerRequest request) {
    switch (request.getMethodName()) {
      case CREATE:
        LASClassManagerRequest.LASClassManagerCreateRequest<T> createRequest = request.asCreateRequest(entityClazz);
        Response<SaveMsg> createResponse = new LASResponse(SaveMsg.class);
        try {
          SaveResult saveResult = entityManager.create(createRequest.getEntity(), request.getUserPrincipal());
          if (saveResult.isSuccess()) {
            createResponse.setResult(saveResult.getSaveMessage());
          } else {
            createResponse.setError(saveResult.getFailMessage());
          }
        } catch (Exception e) {
          createResponse.setError(e.getMessage());
        }
        return createResponse;
      case UPDATE:
        LASClassManagerRequest.LASClassManagerUpdateRequest updateRequest = request.asUpdateRequest();
        Response<UpdateMsg> updateResponse = new LASResponse<UpdateMsg>(UpdateMsg.class);
        try {
          UpdateMsg updateMsg = entityManager.update(updateRequest.getObjectId(), updateRequest.lasUpdate(), request.getUserPrincipal());
          updateResponse.setResult(updateMsg);
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
          updateResponse.setError(e.getMessage());
        }
        return updateResponse;
      case DELETE:
        LASClassManagerRequest.LASClassManagerDeleteRequest deleteRequest = request.asDeleteRequest();
        Response<DeleteMsg> deleteResponse = new LASResponse(DeleteMsg.class);
        try {
          DeleteResult deleteResult = entityManager.delete(deleteRequest.getObjectId(), request.getUserPrincipal());
          if (deleteResult.isSuccess()) {
            deleteResponse.setResult(deleteResult.getDeleteMessage());
          } else {
            deleteResponse.setError(deleteResult.getFailMessage());
          }
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
          deleteResponse.setError(e.getMessage());
        }
        return deleteResponse;
      case DELETEBATCH:
        LASClassManagerRequest.LASClassManagerDeleteBatchRequest deleteBatchRequest = request.asDeleteBatchRequest();
        Response<DeleteMsg> deleteBatchResponse = new LASResponse(DeleteMsg.class);
        try {
          DeleteResult deleteResult = entityManager.delete(deleteBatchRequest.getObjectIds(), request.getUserPrincipal());
          deleteBatchResponse.setResult(deleteResult.getDeleteMessage());
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
          deleteBatchResponse.setError(e.getMessage());
        }
        return deleteBatchResponse;
      default:
        throw new IllegalStateException("the method can not be matched. " + request.getMethod());
    }
  }
}