import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './library.reducer';

export const LibraryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const libraryEntity = useAppSelector(state => state.library.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="libraryDetailsHeading">
          <Translate contentKey="schoolManagementApp.library.detail.title">Library</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{libraryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="schoolManagementApp.library.name">Name</Translate>
            </span>
          </dt>
          <dd>{libraryEntity.name}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="schoolManagementApp.library.code">Code</Translate>
            </span>
          </dt>
          <dd>{libraryEntity.code}</dd>
          <dt>
            <span id="block">
              <Translate contentKey="schoolManagementApp.library.block">Block</Translate>
            </span>
          </dt>
          <dd>{libraryEntity.block}</dd>
          <dt>
            <span id="isVisible">
              <Translate contentKey="schoolManagementApp.library.isVisible">Is Visible</Translate>
            </span>
          </dt>
          <dd>{libraryEntity.isVisible ? 'true' : 'false'}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="schoolManagementApp.library.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{libraryEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="schoolManagementApp.library.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{libraryEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="schoolManagementApp.library.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {libraryEntity.createdDate ? <TextFormat value={libraryEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="schoolManagementApp.library.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{libraryEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="schoolManagementApp.library.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {libraryEntity.lastModifiedDate ? (
              <TextFormat value={libraryEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/library" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/library/${libraryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LibraryDetail;
