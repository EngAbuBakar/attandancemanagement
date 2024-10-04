import student from 'app/entities/student/student.reducer';
import department from 'app/entities/department/department.reducer';
import library from 'app/entities/library/library.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  student,
  department,
  library,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
