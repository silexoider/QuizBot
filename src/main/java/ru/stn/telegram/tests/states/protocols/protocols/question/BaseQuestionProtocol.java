package ru.stn.telegram.tests.states.protocols.protocols.question;

import org.springframework.beans.factory.annotation.Autowired;
import ru.stn.telegram.tests.states.entities.Question;
import ru.stn.telegram.tests.states.entities.sessions.Session;
import ru.stn.telegram.tests.states.protocols.basics.BaseProtocol;
import ru.stn.telegram.tests.states.protocols.basics.Navigator;
import ru.stn.telegram.tests.states.services.QuestionService;

import java.util.ResourceBundle;

public abstract class BaseQuestionProtocol extends BaseProtocol<QuestionContext> {
    @Autowired
    protected QuestionService questionService;

    public BaseQuestionProtocol(Navigator<? super QuestionContext, QuestionContext>... navigators) {
        super(navigators);
    }

    @Override
    public void commit(Session session, QuestionContext context, ResourceBundle resourceBundle) {
        Question question = questionService.find(context.getChatId(), context.getPostId());
        if (commitInternal(session, question, context, resourceBundle)) {
            super.commit(session, context, resourceBundle);
        }
    }

    protected abstract boolean commitInternal(Session session, Question question, QuestionContext context, ResourceBundle resourceBundle);
}
