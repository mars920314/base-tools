# coding=utf-8
# python调用java的代码


from jpype import *
from utils.config import NN_PRO_PATH


class PreprocessService(object):

    def __init__(self,cfg_dict):
        self._cfg_dict = cfg_dict
        self._init_jvm()
        self._analysis_service = JClass(self._cfg_dict['puma_class_name'])

    def __del__(self):
        shutdownJVM()

    def _init_jvm(self):
        jar_path = NN_PRO_PATH + self._cfg_dict['puma_path']
        param_str = '-Djava.class.path=' + jar_path
        jvm_path = getDefaultJVMPath()
        startJVM(jvm_path, '-ea', param_str)

    def _build_answer_from_jar_ret(self, jar_ret):
        ret = dict()
        jar_ret_lst = jar_ret.split('|')
        if len(jar_ret_lst) == 1:
            ret['date_question'] = jar_ret
            ret['words'] = []
            ret['date_words'] = []
        else:
            date_question = jar_ret_lst[0]
            words_str = jar_ret_lst[1]
            date_words_str = jar_ret_lst[2]
            words = words_str.split(',')
            date_words = date_words_str.split(',')
            ret['date_question'] = date_question
            ret['words'] = words
            ret['date_words'] = date_words
        return ret

    def _date_recog(self, words):
        ret = dict()
        cut_question = ','.join(words)
        result_str = self._analysis_service.getDateRangeFeatureForQuestion(cut_question)
        result = self._build_answer_from_jar_ret(result_str)
        ret['date_question'] = result['date_question']
        ret['date_words'] = result['words']
        ret['date_range_words'] = result['date_words']
        ret['words'] = words
        return ret

if __name__ == '__main__':
    service = PreprocessService(None)
    result = service.date_recog(u'2018年的研报')
