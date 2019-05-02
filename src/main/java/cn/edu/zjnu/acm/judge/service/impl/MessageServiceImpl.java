/*
 * Copyright 2016 ZJNU ACM.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.edu.zjnu.acm.judge.service.impl;

import cn.edu.zjnu.acm.judge.domain.Message;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.MessageMapper;
import cn.edu.zjnu.acm.judge.service.MessageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zhanhb
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;

    @Autowired
    public MessageServiceImpl(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Transactional
    @Override
    public void save(Long parentId, Long problemId, String userId, String title, String content) {
        long depth = 0;
        long order = 0;

        final long nextId = messageMapper.nextId();
        Message parent = null;
        if (parentId != null) {
            parent = messageMapper.findOne(parentId);
            if (parent == null) {
                throw new BusinessException(BusinessCode.MESSAGE_NO_SUCH_PARENT, parentId);
            }
            order = parent.getOrder();
            final long depth1 = parent.getDepth();

            List<Message> messages = messageMapper.findAllByThreadIdAndOrderNumGreaterThanOrderByOrderNum(parent.getThread(), parent.getOrder());
            for (Message m : messages) {
                depth = m.getDepth();
                if (depth <= depth1) {
                    break;
                }
                order = m.getOrder();
            }
            depth = depth1 + 1;
            messageMapper.updateOrderNumByThreadIdAndOrderNumGreaterThan(parent.getThread(), order);
            ++order;
        }
        messageMapper.save(nextId, parentId, order, problemId, depth, userId, title, content);
        if (parent != null) {
            messageMapper.updateThreadIdByThreadId(nextId, parent.getThread());
        }
    }

}
