package com.itechartgroup.sms.service;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Collection;

import static org.apache.commons.collections4.CollectionUtils.intersection;

public final class Util {

    private Util() {
    }

    /**
     * Converts graph's edge into users match.
     *
     * @param edge edge
     * @param graph edge's graph
     * @return match
     */
    public static Match convertToUsersMatch(DefaultWeightedEdge edge, Graph<User, DefaultWeightedEdge> graph) {
        String left = graph.getEdgeSource(edge).getName();
        String right = graph.getEdgeTarget(edge).getName();
        Collection<String> commonInterests = intersection(
                graph.getEdgeSource(edge).getInterests(), graph.getEdgeTarget(edge).getInterests());
        return new Match(left, right, commonInterests);
    }
}
