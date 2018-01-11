import {Task} from './models/task';
import {TaskEvaluate} from './models/task-evaluate';

export function taskEvaluateChartOptions(task: Task, taskEvaluate: TaskEvaluate): any {
  const fields = task && task.evaluateTemplate && task.evaluateTemplate.fields;
  if (!(fields && fields.length > 0)) {
    return null;
  }
  const fieldsValue = taskEvaluate && taskEvaluate.fieldsValue;
  if (!(fieldsValue && fieldsValue.length > 0)) {
    return null;
  }
  const xAxisData = [];
  const seriesData = [];
  fields.forEach(field => {
    xAxisData.push(field.name);
    const fieldValue = fieldsValue.find(it => it.id === field.id);
    seriesData.push(fieldValue.value || 0);
  });
  return {
    tooltip: {},
    xAxis: {
      data: xAxisData,
      axisTick: {
        alignWithLabel: true
      }
    },
    yAxis: {},
    series: [{
      type: 'bar',
      label: {
        normal: {
          show: true
        }
      },
      data: seriesData
    }]
  };
}
