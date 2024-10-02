import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './department.reducer';

export const DepartmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const departmentEntity = useAppSelector(state => state.department.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="departmentDetailsHeading">
          <Translate contentKey="schoolManagementApp.department.detail.title">Department</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{departmentEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="schoolManagementApp.department.name">Name</Translate>
            </span>
          </dt>
          <dd>{departmentEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="schoolManagementApp.department.description">Description</Translate>
            </span>
          </dt>
          <dd>{departmentEntity.description}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="schoolManagementApp.department.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {departmentEntity.createdDate ? <TextFormat value={departmentEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="schoolManagementApp.department.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {departmentEntity.lastModifiedDate ? (
              <TextFormat value={departmentEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="schoolManagementApp.department.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{departmentEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="schoolManagementApp.department.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{departmentEntity.lastModifiedBy}</dd>
        </dl>
        <Button tag={Link} to="/department" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/department/${departmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DepartmentDetail;
