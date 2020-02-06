package com.itechartgroup.sms.service;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.itechartgroup.sms.service.Util.convertToUsersMatch;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.intersection;

@Service
public class SMService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMService.class);

    public List<Match> match(List<User> users) {
        Graph<User, DefaultWeightedEdge> graph = buildGraph(users);
        LOGGER.info("Graph has been built: {}", graph);

        Set<Set<DefaultWeightedEdge>> allPairs = getAllPairs(graph);

        Set<DefaultWeightedEdge> maxWeightPairs =
                allPairs.stream().max(comparingInt(s -> getEdgesWeight(s, graph)))
                        .orElse(new HashSet<>());

        return maxWeightPairs.stream().map(edge -> convertToUsersMatch(edge, graph)).collect(toList());
    }

    private Set<Set<DefaultWeightedEdge>> getAllPairs(Graph<User, DefaultWeightedEdge> graph) {
        Set<Set<DefaultWeightedEdge>> allPairs = new HashSet<>();

        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            LOGGER.info("Visiting edge {}", edge);
            Set<DefaultWeightedEdge> pairs = new LinkedHashSet<>();
            traverseAllEdges(graph, edge, new HashSet<>(), pairs);
            if (!pairs.isEmpty()) {
                allPairs.add(pairs);
            }
        }

        return allPairs;
    }

    private void traverseAllEdges(
            Graph<User, DefaultWeightedEdge> graph, DefaultWeightedEdge startEdge,
            Set<User> visitedUsers, Set<DefaultWeightedEdge> pairs) {

        User sourceUser = graph.getEdgeSource(startEdge);
        User targetUser = graph.getEdgeTarget(startEdge);

        boolean isVisitedEdge = visitedUsers.contains(sourceUser) && visitedUsers.contains(targetUser);

        if (isVisitedEdge) {
            return;
        }

        if (visitedUsers.contains(sourceUser)) {
            traverserTargetUserEdges(targetUser, graph, visitedUsers, pairs);
            return;
        } else if (visitedUsers.contains(targetUser)) {
            traverseSourceUserEdges(sourceUser, graph, startEdge, visitedUsers, pairs);
            return;
        }

        pairs.add(startEdge);
        visitedUsers.add(sourceUser);
        visitedUsers.add(targetUser);

        Set<DefaultWeightedEdge> targetEdges = graph.edgesOf(targetUser);
        for (DefaultWeightedEdge targetEdge : targetEdges) {
            traverseAllEdges(graph, targetEdge, visitedUsers, pairs);
        }
    }

    private void traverserTargetUserEdges(
            User targetUser, Graph<User, DefaultWeightedEdge> graph,
            Set<User> visitedUsers, Set<DefaultWeightedEdge> pairs) {
        LOGGER.info("Visiting target vertex {}", targetUser);
        Set<DefaultWeightedEdge> targetUserEdges = graph.edgesOf(targetUser);
        for (DefaultWeightedEdge targetEdge : targetUserEdges) {
            boolean isNewVertex = graph.getEdgeTarget(targetEdge) != targetUser;
            if (isNewVertex) {
                traverseAllEdges(graph, targetEdge, visitedUsers, pairs);
            }
        }
    }

    private void traverseSourceUserEdges(
            User sourceUser, Graph<User, DefaultWeightedEdge> graph, DefaultWeightedEdge currentEdge,
            Set<User> visitedUsers, Set<DefaultWeightedEdge> pairs) {

        LOGGER.info("Visiting source vertex {}", sourceUser);

        Set<DefaultWeightedEdge> sourceEdges = graph.edgesOf(sourceUser);

        for (DefaultWeightedEdge sourceEdge : sourceEdges) {
            boolean isEdgeVisited = visitedUsers.contains(graph.getEdgeSource(sourceEdge))
                    || visitedUsers.contains(graph.getEdgeTarget(sourceEdge));

            if (sourceEdge != currentEdge && !isEdgeVisited) {
                traverseAllEdges(graph, sourceEdge, visitedUsers, pairs);
            }
        }
    }

    private Graph<User, DefaultWeightedEdge> buildGraph(List<User> users) {
        Graph<User, DefaultWeightedEdge> graph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);

        for (User newVertex : users) {
            addVertex(newVertex, graph);
        }

        return graph;
    }

    private void addVertex(User newVertex, Graph<User, DefaultWeightedEdge> graph) {
        Set<User> graphVertexes = new HashSet<>(graph.vertexSet());

        graph.addVertex(newVertex);

        for (User existingVertex : graphVertexes) {
            int commonInterests = intersection(existingVertex.getInterests(), newVertex.getInterests()).size();
            if (commonInterests > 0) {
                DefaultWeightedEdge newEdge = graph.addEdge(existingVertex, newVertex);
                graph.setEdgeWeight(newEdge, commonInterests);
            }
        }
    }

    private int getEdgesWeight(Set<DefaultWeightedEdge> edges, Graph<User, DefaultWeightedEdge> graph) {
        return edges.stream().mapToInt(edge -> Double.valueOf(graph.getEdgeWeight(edge)).intValue()).sum();
    }

}
