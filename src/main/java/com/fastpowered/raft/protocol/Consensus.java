package com.fastpowered.raft.protocol;

import com.fastpowered.raft.dto.AentryParam;
import com.fastpowered.raft.dto.AentryResult;
import com.fastpowered.raft.dto.RvoteParam;
import com.fastpowered.raft.dto.RvoteResult;

/**
 * 一致性模块接口
 */
public interface Consensus {

    /**
     * 候选人请求投票
     *
     * 1. term < currentTerm 返回 false, 候选人的任期号不能小于投票人的任期号
     * 2. votedFor == null || votedFor == candidateId && 候选人的日志至少和自己一样新，那么投票给候选人
     *
     * @param param
     * @return
     */
    RvoteResult requestVote(RvoteParam param);

    /**
     * Leader 向其它节点附加日志，如果当前非Leader，将请求转发给Leader处理
     *
     * 1. 如果 term < currentTerm 就返回 false
     * 2. 如果日志在 prevLogIndex 位置处的日志条目的任期号和 prevLogTerm 不匹配，则返回 false
     * 3. 如果已经存在的日志条目和新的产生冲突（索引值相同但是任期号不同），删除这一条和之后所有的
     * 4. 附加任何在已有的日志中不存在的条目
     * 5. 如果 leaderCommit > commitIndex，令 commitIndex 等于 leaderCommit 和 新日志条目索引值中较小的一个
     *
     * @param param
     * @return
     */
    AentryResult appendEntries(AentryParam param);

}
